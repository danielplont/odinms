/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sf.odinms.exttools.exepatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PatchMapleEXE {
	private static boolean compareByteArray(byte[] a, byte[] b, int starta, int startb, int length) {
		int i = starta;
		int j = startb;
		for (int x = 0; x < length; x++) {
			if (a[i] != b[j]) {
				return false;
			}
			i++;
			j++;
		}
		return true;
	}

	private static byte[] padByteArray(byte[] in, int length) {
		byte[] ret = new byte[length];
		for (int x = 0; x < length; x++) {
			if (x < in.length) {
				ret[x] = in[x];
			} else {
				ret[x] = 0;
			}
		}
		return ret;
	}

	private static byte[] getAsciiByteArray(String s) {
		byte[] ret = new byte[s.length()];
		for (int x = 0; x < s.length(); x++) {
			ret[x] = (byte) s.charAt(x);
		}
		return ret;
	}

	public static void main(String args[]) throws IOException {
		final byte startgg[] = new byte[] { 0x52, 0x50, 0x6A, 0x00, 0x6A, 0x00, 0x6A, 0x00, 0x6A, 0x01, 0x6A, 0x00,
											0x6A, 0x00 };
		final byte startgg_new[] = new byte[] { 0x52, 0x50, 0x6A, 0x00, 0x6A, 0x00, 0x6A, 0x00, 0x6A, 0x00, 0x6A, 0x00,
												0x6A, 0x00 };
		final byte startgg_mov[] = new byte[] { (byte) 0xB8, 0x62, 0x02, 00, 00 };
		final byte startgg_mov_new[] = new byte[] { (byte) 0xB8, 0x55, 0x07, 00, 00 };

		boolean patchedgg1 = false;
		boolean patchedgg2 = false;
		boolean patchedips[] = new boolean[3];
		//String oldIps[] = { "63.251.217.4", "63.251.217.3", "63.251.217.2" };
                //String oldIps[] = { "78.47.155.10", "78.47.155.10", "78.47.155.10" };
		String oldIps[] = { "login.odinms.de", "login.odinms.de", "login.odinms.de" };
		//String oldIps[] = { "127.0.0.1", "127.0.0.1", "127.0.0.1" };
		//String newIp_ = "209.160.33.9"; //dual sailr
                String newIp_ = "195.238.149.74"; //localhost
		String newIps[] = { newIp_, newIp_, newIp_ };
		File mapleExe = new File(args[0]);
		RandomAccessFile raf = new RandomAccessFile(mapleExe, "r");

		byte file[] = new byte[(int) raf.length()];

		raf.readFully(file);
		for (int x = 0; x < file.length; x++) {
			if (!patchedgg1 && compareByteArray(file, startgg, x, 0, startgg.length)) {
				System.arraycopy(startgg_new, 0, file, x, startgg_new.length);
				patchedgg1 = true;
				x += startgg_new.length;
			}
			if (!patchedgg2 && compareByteArray(file, startgg_mov, x, 0, startgg_mov.length)) {
				System.arraycopy(startgg_mov_new, 0, file, x, startgg_mov_new.length);
				patchedgg2 = true;
				x += startgg_mov_new.length;
			}
			for (int y = 0; y < patchedips.length; y++) {
				byte ipByte[] = padByteArray(getAsciiByteArray(oldIps[y]), 16);
				if (!patchedips[y] && compareByteArray(file, ipByte, x, 0, ipByte.length)) {
					byte newIp[] = padByteArray(getAsciiByteArray(newIps[y]), 16);
					System.arraycopy(newIp, 0, file, x, newIp.length);
					patchedips[y] = true;
					x += newIps[y].length();
				}
			}
		}
		System.out.println("Patched GG1: " + patchedgg1);
		System.out.println("Patched GG2: " + patchedgg2);
		for (int x = 0; x < newIps.length; x++) {
			System.out.println("Patched IP[" + x + "]: " + patchedips[x]);
		}

		File out = new File(args[1]);
		FileOutputStream fos = new FileOutputStream(out);
		fos.write(file);
	}
}
