package minesweeper.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import minesweeper.enumeration.SquareState;

@Data
@EqualsAndHashCode
public class Square
{
    private int         row;
    private int         col;
    private SquareState state;
    private int         mineNum;





    public Square(final int row, final int col)
    {
        this.row = row;
        this.col = col;
        this.state = SquareState.CLOSE;
        this.mineNum = 0;
    }





    public static Square of(final int row, final int col)
    {
        return new Square(row, col);
    }





    public void open()
    {
        this.state = SquareState.OPEN;
    }





    public void putMine()
    {
        this.mineNum = -1;
    }





    public void hasMineNearby()
    {
        this.mineNum++;
    }

}
