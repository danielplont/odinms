package net.sf.odinms.exttools.wzextract;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageOutputStream;

import net.sf.odinms.provider.MapleCanvas;
import net.sf.odinms.provider.MapleData;
import net.sf.odinms.provider.MapleDataDirectoryEntry;
import net.sf.odinms.provider.MapleDataEntry;
import net.sf.odinms.provider.MapleDataProvider;
import net.sf.odinms.provider.MapleDataProviderFactory;

public class XMLWzExtract {
	private boolean dumpPNG;
	private ImageWriter writer = null;
	private File pngBaseDir = null;
	private boolean hadError = false;
	
	public XMLWzExtract(boolean dumpPNG) throws Exception {
		this.dumpPNG = dumpPNG;
		if (dumpPNG) {
			IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
			Iterator<ImageWriterSpi> serviceProviders = iioRegistry.getServiceProviders(javax.imageio.spi.ImageWriterSpi.class, false);
			outer:
			while(serviceProviders.hasNext()) {
				ImageWriterSpi writerSpi = serviceProviders.next();
				for (String name : writerSpi.getFormatNames()) {
					if (name.equalsIgnoreCase("png")) {
						writer = writerSpi.createWriterInstance();
						break outer;
					}
				}
			}
		}
	}

	public void extractWZ(String wzName, File outputBaseDirectory) {
		if (!outputBaseDirectory.exists()) {
			outputBaseDirectory.mkdir();
		} else {
			//System.out.println("skipping " + wzName + " (directory exists)");
		}
		MapleDataProvider dataProv;
		if (dumpPNG) {
			dataProv = MapleDataProviderFactory.getImageProvidingDataProvider(new File(wzName));
		} else {
			dataProv = MapleDataProviderFactory.getDataProvider(new File(wzName));
		}
		MapleDataDirectoryEntry root = dataProv.getRoot();
		
		dumpDirectory(dataProv, "", root, outputBaseDirectory);
	}

	private void dumpDirectory(MapleDataProvider dataProv, String path, MapleDataDirectoryEntry root,
								File outputBaseDirectory) {
		File file = new File(outputBaseDirectory, root.getName());
		file.mkdir();
		for (MapleDataEntry entry : root.getFiles()) {
			File xmlOutFile = new File(file, entry.getName() + ".xml");
			try {
				String filePath = path;
				if (filePath.length() > 0) {
					filePath += "/";
				}
				filePath += entry.getName();
				xmlOutFile.createNewFile();
				pngBaseDir = file;
				System.out.println("Dumping file " + filePath + " from " + dataProv.getRoot().getName() + " to " +
					xmlOutFile.getPath());
				dumpImg(dataProv.getData(filePath), new FileOutputStream(xmlOutFile));
			} catch (FileNotFoundException e) {
				hadError = true;
				e.printStackTrace();
			} catch (IOException e) {
				hadError = true;
				e.printStackTrace();
			}
		}
		for (MapleDataDirectoryEntry child : root.getSubdirectories()) {
			dumpDirectory(dataProv, path + (path.equals("") ? "" : "/") + child.getName(), child, file);
		}
	}

	private void dumpImg(MapleData wzFile, OutputStream os) {
		PrintWriter pw = new PrintWriter(os);
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		dumpData(wzFile, pw, 0, "");
		pw.flush();
	}

	private void dumpData(MapleData data, PrintWriter pw, int level, String pathInImg) {
		switch (data.getType()) {
			case PROPERTY:
				pw.println(indentation(level) + openNamedTag("imgdir", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1, pathInImg + data.getName() + "/");
				pw.println(indentation(level) + closeTag("imgdir"));
				break;
			case EXTENDED:
				pw.println(indentation(level) + openNamedTag("extended", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1, pathInImg + data.getName() + "/");
				pw.println(indentation(level) + closeTag("extended"));
				break;
			case CANVAS:
				MapleCanvas canvas = (MapleCanvas) data.getData();
				pw.println(indentation(level) + openNamedTag("canvas", data.getName(), false, false) +
					attrib("width", Integer.toString(canvas.getWidth())) +
					attrib("height", Integer.toString(canvas.getHeight()), true, false));
				
				if (dumpPNG) {
					File pngDir = new File(pngBaseDir, pathInImg);
					if (!pngDir.exists()) {
						pngDir.mkdirs();
					}
					File pngFile = new File(pngDir, data.getName() + ".png");
					System.out.println("Dumping canvas data to " + pngFile.getAbsolutePath());
					FileImageOutputStream fileImageOutputStream = null;
					try {
						fileImageOutputStream = new FileImageOutputStream(pngFile);
						writer.setOutput(fileImageOutputStream);
						writer.write(canvas.getImage());
					} catch (FileNotFoundException e) {
						hadError = true;
						e.printStackTrace();
					} catch (IOException e) {
						hadError = true;
						e.printStackTrace();
					} finally {
						try {
							if (fileImageOutputStream != null) {
								fileImageOutputStream.close();
							}
						} catch (IOException e) {
							hadError = true;
							e.printStackTrace();
						}
					}
				}
				
				dumpDataList(data.getChildren(), pw, level + 1, pathInImg + data.getName() + "/");
				pw.println(indentation(level) + closeTag("canvas"));
				break;
			case CONVEX:
				pw.println(indentation(level) + openNamedTag("convex", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1, pathInImg + data.getName() + "/");
				pw.println(indentation(level) + closeTag("convex"));
				break;
			case SOUND:
				pw.println(indentation(level) + emptyNamedTag("sound", data.getName()));
				break;
			case UOL:
				pw.println(indentation(level) + emptyNamedValuePair("uol", data.getName(), data.getData().toString()));
				break;
			case DOUBLE:
				pw.println(indentation(level) +
					emptyNamedValuePair("double", data.getName(), data.getData().toString()));
				break;
			case FLOAT:
				pw.println(indentation(level) + emptyNamedValuePair("float", data.getName(), data.getData().toString()));
				break;
			case INT:
				pw.println(indentation(level) + emptyNamedValuePair("int", data.getName(), data.getData().toString()));
				break;
			case SHORT:
				pw.println(indentation(level) + emptyNamedValuePair("short", data.getName(), data.getData().toString()));
				break;
			case STRING:
				pw.println(indentation(level) +
					emptyNamedValuePair("string", data.getName(), data.getData().toString()));
				break;
			case VECTOR:
				Point tPoint = (Point) data.getData();
				pw.println(indentation(level) + openNamedTag("vector", data.getName(), false, false) +
					attrib("x", Integer.toString(tPoint.x)) + attrib("y", Integer.toString(tPoint.y), true, true));
				break;
			case IMG_0x00:
				pw.println(indentation(level) + emptyNamedTag("null", data.getName()));
				break;
			default:
				throw new RuntimeException("Unexpected img data type " + data.getType() + " path: " + pathInImg);
		}
	}

	private void dumpDataList(List<MapleData> datalist, PrintWriter pw, int level, String pathInImg) {
		for (MapleData data : datalist) {
			dumpData(data, pw, level, pathInImg);
		}
	}

	private String openNamedTag(String tag, String name, boolean finish) {
		return openNamedTag(tag, name, finish, false);
	}

	private String emptyNamedTag(String tag, String name) {
		return openNamedTag(tag, name, true, true);
	}

	private String emptyNamedValuePair(String tag, String name, String value) {
		return openNamedTag(tag, name, false, false) + attrib("value", value, true, true);
	}

	private String openNamedTag(String tag, String name, boolean finish, boolean empty) {
		return "<" + tag + " name=\"" + name + "\"" + (finish ? (empty ? "/>" : ">") : " ");
	}

	private String attrib(String name, String value) {
		return attrib(name, XmlUtil.sanitizeText(value), false, false);
	}

	private String attrib(String name, String value, boolean closeTag, boolean empty) {
		return name + "=\"" + XmlUtil.sanitizeText(value) + "\"" + (closeTag ? (empty ? "/>" : ">") : " ");
	}

	private String closeTag(String tag) {
		return "</" + tag + ">";
	}

	private String indentation(int level) {
		char[] indent = new char[level];
		for (int i = 0; i < indent.length; i++) {
			indent[i] = '\t';
		}
		return new String(indent);
	}

	public boolean isHadError() {
		return hadError;
	}

	public static void main(String args[]) {
		boolean dumpPng = false;
		boolean hadError = false;
		long startTime = System.currentTimeMillis();
		for (String file : args) {
			if (file.equals("-dumppng")) {
				dumpPng = true;
			} else {
				try {
					XMLWzExtract wzExtract = new XMLWzExtract(dumpPng);
					wzExtract.extractWZ(file, new File("xmlout"));
					hadError |= wzExtract.isHadError();
				} catch (Exception e) {
					hadError = true;
					System.out.println("Exception occured while dumping " + file + " continuing with next file");
					System.out.flush();
					e.printStackTrace();
				}
			}
		}
		long endTime = System.currentTimeMillis();
		double elapsedSeconds = (endTime - startTime) / 1000.0;
		int elapsedSecs = (((int) elapsedSeconds) % 60);
		int elapsedMinutes = (int) (elapsedSeconds / 60.0);
		
		String withErrors = "";
		if (hadError) {
			withErrors = " with errors";
		}
		System.out.println("Finished" + withErrors + " in " + elapsedMinutes + " minutes " + elapsedSecs + " seconds");
	}
}
