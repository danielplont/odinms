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

package net.sf.odinms.exttools.shopcreator;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 *
 * @author andy
 */
public class ShopItemListCellRenderer extends JLabel implements ListCellRenderer
{
	public ShopItemListCellRenderer()
	{
		setOpaque(true);
	}
       
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		ShopItem val = (ShopItem)value;
		setText(val.getValue());
		setIcon(val.getIcon());
		
		//setBackground(isSelected ? Color.getHSBColor(2f, 0.3f, 0.1f) : (index & 1) == 0 ? Color.cyan : Color.green);
		//setForeground(isSelected ? Color.white : Color.black);
		
		return this;
	}
}
