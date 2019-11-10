package minesweeper;

import java.util.Scanner;

import minesweeper.domain.GameInfo;
import minesweeper.domain.Square;
import minesweeper.enumeration.GameState;
import minesweeper.enumeration.SquareState;

public class Game
{

    public static Scanner sc = new Scanner(System.in);





    public static void main(final String[] args)
    {
        // 판 사이즈, 지뢰 갯수 입력
        GameInfo info = inputSize();
        GameRound round = null;
        String restart = "N";
        do
        { // 게임 재시작
            try
            {
                // 게임생성
                round = new GameRound(info.getBoardSize(), info.getMineSize());
                // 게임설명 print
                printDescription(info.getBoardSize(), info.getMineSize());
            }
            catch ( Exception e )
            {
                System.out.println("(!) 게임 생성 중 오류가 발생하였습니다..");
                e.printStackTrace();
                round.setGameState(GameState.GAMEOVER);

            }
            do
            { // 오픈할 칸 입력

                try
                {
                    System.out.println("오픈할 칸 입력 ( X , Y ) : ");
                    String input = sc.nextLine();
                    if ( "e".equals(input.trim()) )
                    {
                        System.out.println("게임 종료..");
                        round.setGameState(GameState.GAMEOVER);
                    }
                    if ( !input.contains(",") )
                    {
                        System.out.println("값을 ( X , Y ) 형태로 입력해주세요");
                        continue;
                    }

                    int x = Integer.parseInt(input.split(",")[0].replace("(", "")
                                                                .trim())
                            - 1;
                    int y = Integer.parseInt(input.split(",")[1].replace(")", "")
                                                                .trim())
                            - 1;

                    if ( x < 0 || x > 9 || y < 0 || y > 9 )
                    {
                        System.out.println("X, Y 값은 1 ~ 10 사이의 값으로 입력해주세요.");
                        continue;
                    }

                    // 입력받은 칸 오픈
                    GameState rsltGameSate = round.openSquare(x, y);
                    if ( GameState.PLAYING.equals(rsltGameSate) )
                    {
                        printBoard(round.getBoard());
                    }
                    else
                    {
                        if ( GameState.WIN.equals(rsltGameSate) )
                        {
                            System.out.println("You win! :)");
                        }
                        else if ( GameState.GAMEOVER.equals(rsltGameSate) )
                        {
                            System.out.println("Game over! :(");
                        }
                        printBoard(round.getBoard(), true);
                        System.out.print("게임을 다시 진행하시겠습니까?(Y/N) ");
                        restart = sc.nextLine()
                                    .trim();
                    }

                }
                catch ( Exception e )
                {
                    System.out.println("(!) 게임 진행 중 오류가 발생하였습니다..");
                    e.printStackTrace();
                    round.setGameState(GameState.GAMEOVER);
                }
            }
            while ( !GameState.GAMEOVER.equals(round.getGameState()) );
        }
        while ( "Y".equals(restart) );
    }





    /**
     * 게임 기초 데이터 입력
     **/
    private static GameInfo inputSize()
    {

        // 변수
        System.out.print("판 사이즈를 입력하세요 : ");
        int boardSize = Integer.parseInt(sc.nextLine());
        System.out.print("지뢰의 갯수를 입력하세요 : ");
        int mineSize = Integer.parseInt(sc.nextLine());

        return new GameInfo(boardSize, mineSize);
    }





    /**
     * 게임 설명
     **/
    public static void printDescription(final int boardSize, final int mineSize)
    {
        System.out.println("");
        System.out.println("***** 게임 설명 *****");
        System.out.println("판 사이즈 : " + boardSize + "*" + boardSize + ", 지뢰 갯수 " + mineSize);
        System.out.println("");
        System.out.println("[+] : 열지 않은상태");
        System.out.println("[B] : 지뢰");
        System.out.println("[숫자] : 자신을 제외한 주변 칸 8개 중 지뢰의 갯수");
        System.out.println("");
        System.out.println(">>> Start!!!");
    }





    /**
     * 판 출력
     *
     * @param board
     *            판(이차원 배열)
     */
    public static void printBoard(final Square[][] board)
    {
        printBoard(board, false);
    }





    /**
     * 판 출력
     *
     * @param board판(이차원
     *            배열)
     * @param allPrint
     *            전체 출력 여부
     *            - true open 상태인 칸 + 지뢰 open
     *            - false open 상태인 칸만 open
     */
    public static void printBoard(final Square[][] board, final boolean allPrint)
    {
        for ( Square[] col : board )
        {
            int lastRow = 0;
            for ( Square row : col )
            {
                if ( allPrint )
                {
                    if ( -1 == row.getMineNum() )
                    {
                        System.out.print(" B ");
                    }
                    else if ( SquareState.OPEN.equals(row.getState()) )
                    {
                        System.out.print(" " + row.getMineNum() + " ");
                    }
                    else
                    {
                        System.out.print(" + ");
                    }
                }
                else
                {
                    if ( SquareState.OPEN.equals(row.getState()) )
                    {
                        if ( -1 == row.getMineNum() )
                        {
                            System.out.print(" B ");
                        }
                        else
                        {
                            System.out.print(" " + row.getMineNum() + " ");
                        }
                    }
                    else
                    {
                        System.out.print(" + ");
                    }
                }

                if ( ++lastRow == col.length )
                {
                    System.out.println();
                }
            }
        }

    }

}
