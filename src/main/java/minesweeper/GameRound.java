package minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Data;
import minesweeper.domain.Square;
import minesweeper.enumeration.GameState;
import minesweeper.enumeration.SquareState;

@Data
public class GameRound
{
    private final Square[][]   board;
    private final int          boardSize;
    private final int          mineSize;
    private GameState          gameState;

    private static final int[] deltaX = { -1, 0, 1, 1, 1, 0, -1, -1 };
    private static final int[] deltaY = { -1, -1, -1, 0, 1, 1, 1, 0 };
    private final List<Square> closedList;





    /**
     * 게임판 초기화(지뢰 생성, 판 숫자 설정)
     *
     * @param boardSize
     *            판 사이즈
     * @param mineSize
     *            지뢰 갯수
     */
    public GameRound(final int boardSize, final int mineSize)
    {
        this.boardSize = boardSize;
        this.mineSize = mineSize;
        this.gameState = GameState.playing;

        // 판 생성 & 초기화
        this.board = new Square[boardSize][boardSize];
        // 열리지 않은 칸 담길 리스트 생성 & 초기화
        this.closedList = new ArrayList<Square>();
        for ( int i = 0; i < boardSize; i++ )
        {
            for ( int j = 0; j < boardSize; j++ )
            {
                this.board[j][i] = Square.of(i, j);
                this.closedList.add(this.board[j][i]);
            }
        }

        int cnt = 0;
        while ( cnt < mineSize )
        {
            int x = getRandomNumber(mineSize); // 난수 생성 0 ~ mineSize-1
            int y = getRandomNumber(mineSize); // 난수 생성 0 ~ mineSize-1

            if ( -1 != this.board[y][x].getMineNum() )
            {
                System.out.println("지뢰 : " + y + "," + x);
                this.board[y][x].putMine(); // 지뢰 설정
                this.closedList.remove(this.board[y][x]);

                // 생성된 지뢰를 기준으로 주변 지뢰 수 계산
                for ( Square sq : getSquareNearby(x, y) )
                {
                    int i = sq.getRow();
                    int j = sq.getCol();
                    if ( -1 != this.board[j][i].getMineNum() )
                    {
                        // 주변지뢰수 값 + 1
                        System.out.println(j + "," + i);
                        this.board[j][i].hasMineNearby();
                    }
                }
                System.out.println();
                cnt++;
            }
        }
        System.out.println("cnt" + "=====" + cnt);

    }





    /**
     * 한 칸 열기(입력받은 칸 열기)
     *
     * @param x
     *            입력받은 row 값
     * @param y
     *            입력받은 col 값
     */
    public GameState openSquare(final int x, final int y)
    {
        if ( SquareState.close.equals(this.board[y][x].getState()) )
        {
            this.board[y][x].open();
            this.closedList.remove(this.board[y][x]);
        }
        if ( -1 == this.board[y][x].getMineNum() )
        {
            this.gameState = GameState.gameover;
        }
        else if ( 0 == this.board[y][x].getMineNum() )
        {
            openEmptyBox(x, y);
        }

        if ( this.closedList.size() == 0 )
        {
            this.gameState = GameState.win;
        }
        return this.gameState;
    }





    /**
     * 빈 칸 열기(주변 모든 칸에 지뢰 없는 경우)
     *
     * @param x
     *            입력받은 row 값
     * @param y
     *            입력받은 col 값
     */
    private void openEmptyBox(final int x, final int y)
    {
        // 빈 칸을 기준으로 주변 칸 검증
        for ( Square sq : getSquareNearby(x, y) )
        {
            int i = sq.getRow();
            int j = sq.getCol();
            // 대상 검증 : 사방 8개 칸 중 사각형(board) 내 범위에 있는 칸 모두 비어 있는 경우 열기
            if ( SquareState.close.equals(this.board[j][i].getState()) )
            {
                this.board[j][i].open();
                this.closedList.remove(this.board[j][i]);
                if ( 0 == this.board[j][i].getMineNum() )
                {
                    openEmptyBox(i, j);
                }
            }
        }

    }





    /**
     * 지뢰 위치 설정 용 랜덤 숫자 생성
     *
     * @param mineSize
     *            지뢰 갯수
     */
    private int getRandomNumber(final int mineSize)
    {
        Random random = new Random();
        return random.nextInt(mineSize);
    }





    /**
     * (x,y) 주변 8개 칸 리스트 조회
     *
     * @param x
     *            입력받은 row 값
     * @param y
     *            입력받은 col 값
     */
    private List<Square> getSquareNearby(final int x, final int y)
    {
        List<Square> nearbyList = new ArrayList<Square>();
        for ( int i = 0; i < 8; i++ )
        {
            if ( y - deltaY[i] >= 0 && x - deltaX[i] >= 0 && y - deltaY[i] < this.boardSize && x - deltaX[i] < this.boardSize )
            {
                nearbyList.add(this.board[y - deltaY[i]][x - deltaX[i]]);
            }
        }
        return nearbyList;
    }

}
