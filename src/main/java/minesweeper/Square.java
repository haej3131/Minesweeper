package minesweeper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Square {
	private int row;
	private int col;
	private boolean isMine;
	private boolean isOpen;
	private int mineNum;



	public Square (final int row, final int col) {
		this.row = row;
		this.col = col;
		this.isMine = false;
		this.isOpen = false;
		this.mineNum = 0;
	}

	static Square of(final int row, final int col) {
		return new Square(row, col);
	}

	public void open(){
		this.isOpen = true;
	}

	public void putMine() {
		this.isMine = true;
		this.mineNum = -1;
	}

	public void hasNearMine(){
		this.mineNum++;
	}

}
