package gui;

import java.util.EnumSet;
import java.util.Random;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.RGB;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import script.Characters;
import script.Foe;
import script.Hero;

public class Map extends Panel {

	public static final int COLUMNS = 50;
	public static final int LINES = 16;
	
	public static final int TREECOUNT = 400;
	public static final int BRANCHESCOUNT = 250;
	
	Random mRand;
	
	int[] playerpos = new int[]{2, 2};
	
	int[] waterpos = new int[LINES];
        int[] waterpos2 = new int[COLUMNS];
        int[][] lakepos = new int[5][15];
	Tree[] treespos = new Tree[TREECOUNT];
	Tree[] branchespos = new Tree[BRANCHESCOUNT];
	RGB bkgColor = new TextColor.RGB(165, 127, 61);
        int [] bridgepos = new int[2];
        RGB bridge_Color = new TextColor.RGB(195, 92, 52);
	
	Characters _chars;

	EmptySpace land;

	public Map(Characters chars) {
		super();

		_chars = chars;
		getBasePane();
		
		mRand = new Random();
		
		generateWater();
		generateTrees();

		land = new EmptySpace(new TextColor.RGB(165, 127, 61)) {
			protected ComponentRenderer<EmptySpace> createDefaultRenderer() {
				return new ComponentRenderer<EmptySpace>() {
					public TerminalSize getPreferredSize(EmptySpace component) {
						return new TerminalSize(Map.COLUMNS, Map.LINES);
					}

					public void drawComponent(TextGUIGraphics graphics, EmptySpace component) {
						/*
						 * Fill background
						 */
						graphics.setBackgroundColor(bkgColor);
						graphics.setModifiers(EnumSet.of(SGR.BOLD));
						graphics.fill(' ');
						
						/*
						 * Creates the trees and branches
						 */
						for (Tree t : treespos) {
							graphics.setForegroundColor(t.getColor());
							graphics.putString(t.getmPosition().getColumn(), t.getmPosition().getRow(), String.valueOf(t.getmTree()));
						}
						for (Tree t : branchespos) {
							graphics.setForegroundColor(t.getColor());
							graphics.putString(t.getmPosition().getColumn(), t.getmPosition().getRow(), String.valueOf(t.getmTree()));
						}
                                                
						graphics.setForegroundColor(new TextColor.RGB(30, 150, 200));
                                                int locBridge = mRand.nextInt(waterpos.length);
                                                
                                                /*
						 * Creates the river
						 */
						for (int i = 0; i < waterpos.length; i++) {
							graphics.setBackgroundColor(new TextColor.RGB(30, 150, 100));
							graphics.putString(waterpos[i], i, String.valueOf(SymbolsMirk.WATER[2]));
							graphics.setBackgroundColor(bkgColor);
							graphics.putString(waterpos[i]-1, i, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(waterpos[i]+1, i, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(waterpos[i]-2, i, String.valueOf(SymbolsMirk.WATER[0]));
							graphics.putString(waterpos[i]+2, i, String.valueOf(SymbolsMirk.WATER[0]));
                                                        
                                                        if(i == locBridge){
                                                        graphics.setForegroundColor(bridge_Color);
                                                        graphics.setForegroundColor(bridge_Color);
							graphics.putString(waterpos[i], i, String.valueOf(SymbolsMirk.WATER[2]));
                                                        bridgepos = new int[]{waterpos[i],i};
                                                    }
						}
                                                /*for (int i = 0; i < waterpos2.length; i++) {
							graphics.setBackgroundColor(new TextColor.RGB(30, 150, 100));
							graphics.putString(i, waterpos2[i], String.valueOf(SymbolsMirk.WATER[2]));
							graphics.setBackgroundColor(bkgColor);
							graphics.putString(i, waterpos2[i]-1, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(i, waterpos2[i]+1, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(i, waterpos2[i]-2, String.valueOf(SymbolsMirk.WATER[0]));
							graphics.putString(i, waterpos2[i]+2, String.valueOf(SymbolsMirk.WATER[0]));
                                                        
                                                        if(i == locBridge){
                                                        graphics.setForegroundColor(bridge_Color);
                                                        graphics.setForegroundColor(bridge_Color);
							graphics.putString(i, waterpos2[i], String.valueOf(SymbolsMirk.WATER[2]));
                                                        bridgepos = new int[]{i, waterpos2[i]};
                                                    }
						}*/
                                                
                                                /*
						 * Creates a lake
						 */
						/*for (int i = 0; i < lakepos.length; i++) {
							for(int l = 0; l < i; l++){
                                                        graphics.setBackgroundColor(new TextColor.RGB(30, 150, 150));
							graphics.putString(lakepos[i][l], i, String.valueOf(SymbolsMirk.WATER[2]));
							graphics.setBackgroundColor(bkgColor);
							graphics.putString(lakepos[i][l]-1, i, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(lakepos[i][l]+1, i, String.valueOf(SymbolsMirk.WATER[1]));
							graphics.putString(lakepos[i][l]-2, i, String.valueOf(SymbolsMirk.WATER[0]));
							graphics.putString(lakepos[i][l]+2, i, String.valueOf(SymbolsMirk.WATER[0]));
                                                        }
						}*/
						
						/*
						 * Draw characters
						 */
						Hero h = _chars.getHero();
						graphics.setBackgroundColor(h.get_bkgColor());
						graphics.setForegroundColor(h.get_foregroundColor());
						graphics.setCharacter(h.get_position(), h.get_face());

						graphics.setModifiers(EnumSet.of(SGR.BLINK));
						Foe f = _chars.getFoe();
						graphics.setBackgroundColor(f.get_bkgColor());
						graphics.setForegroundColor(f.get_foregroundColor());
						graphics.setCharacter(f.get_position(), f.get_face());
					}
				};
			}
		};

		addComponent(land);

	}
	
	public void generateWater() {
		int col = mRand.nextInt(COLUMNS);
                int lin = mRand.nextInt(LINES);
		for (int i = 0; i < LINES; i++) {
			waterpos[i] = col + (mRand.nextInt(2) - 1);
		}
                for (int i = 0; i < COLUMNS; i++) {
			waterpos2[i] = lin + (mRand.nextInt(2) - 1);
		}
	}
	
	public void generateTrees() {
		for (int i=0; i < TREECOUNT; i++)
			treespos[i] = Tree.factoryRandomTree(COLUMNS, LINES);

		for (int i=0; i < BRANCHESCOUNT; i++)
			branchespos[i] = Tree.factoryRandomBranch(COLUMNS, LINES);
	}

	public void refreshLand() {
		land.invalidate();
	}
	
	public void updatePlayer(KeyStroke keyStroke) {
		TerminalPosition ppos = _chars.getHero().get_position();
		Hero player = _chars.getHero();
		switch (keyStroke.getCharacter()) {
		case 'w':
			player.set_position(new TerminalPosition(ppos.getColumn(), ppos.getRow() - 1));
			break;
                case 'W':
			player.set_position(new TerminalPosition(ppos.getColumn(), ppos.getRow() - 1));
			break;
		case 's':
			player.set_position(new TerminalPosition(ppos.getColumn(), ppos.getRow()+1));
			break;
                case 'S':
			player.set_position(new TerminalPosition(ppos.getColumn(), ppos.getRow()+1));
			break;
		case 'a':
			player.set_position(new TerminalPosition(ppos.getColumn()-1, ppos.getRow()));
			break;
                case 'A':
			player.set_position(new TerminalPosition(ppos.getColumn()-1, ppos.getRow()));
			break;
		case 'd':
			player.set_position(new TerminalPosition(ppos.getColumn()+1, ppos.getRow()));
			break;
                case 'D':
			player.set_position(new TerminalPosition(ppos.getColumn()+1, ppos.getRow()));
			break;
		default:
			System.out.println(keyStroke.getCharacter().toString());
			break;
		}
		
		refreshLand();
	}

	/*
	 * @Override protected void onAfterDrawing(TextGUIGraphics graphics) { //
	 * TODO Auto-generated method stub super.onAfterDrawing(graphics);
	 * graphics.setForegroundColor(TextColor.ANSI.CYAN);
	 * graphics.setBackgroundColor(TextColor.ANSI.BLUE);
	 * graphics.setModifiers(EnumSet.of(SGR.BOLD)); graphics.fill(' ');
	 * graphics.putString(3, 0, "Text GUI in 100% Java"); }
	 */

}
