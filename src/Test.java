import core.ArcadeMachine;
import tools.Metrics;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 04/10/13
 * Time: 16:29
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Test
{

    public static void main(String[] args)
    {
        //Available controllers:
        String sampleRandomController = "controllers.sampleRandom.Agent";
        String sampleOneStepController = "controllers.sampleonesteplookahead.Agent";
        String sampleMCTSController = "controllers.sampleMCTS.Agent";
        String sampleOLMCTSController = "controllers.sampleOLMCTS.Agent";
        String sampleGAController = "controllers.sampleGA.Agent";

        //Available games:
        String gamesPath = "examples/gridphysics/";

        //CIG 2014 Training Set Games
        String games[] = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs", "missilecommand", "portals", "sokoban", "survivezombies", "zelda"};

        //CIG 2014 Validation Set Gamesxccx
        //String games[] = new String[]{"camelRace", "digdug", "firestorms", "infection", "firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania"};

        //Other settings
        boolean visuals = true;
        String recordActionsFile = null; //where to record the actions executed. null if not to save.
        int seed = new Random().nextInt();

        //Game and level to play
        int gameIdx = 0;
        int levelIdx = 0; //level names from 0 to 4 (game_lvlN.txt).
        String game = gamesPath + games[gameIdx] + ".txt";
        String level1 = gamesPath + games[gameIdx] + "_lvl" + levelIdx +".txt";

        // 1. This starts a game, in a level, played by a human.
        //ArcadeMachine.playOneGame(game, level1, recordActionsFile, seed);

        // 2. This plays a game in a level by the controller.
        //ArcadeMachine.runOneGame(game, level1, visuals, sampleOLMCTSController, recordActionsFile, seed);

        // 3. This replays a game from an action file previously recorded
        //String readActionsFile = "actionsFile_aliens_lvl0.txt";  //This example is for
        //ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);

        // 4. This plays a single game, in N levels, M times :
        //String level2 = gamesPath + games[gameIdx] + "_lvl" + 1 +".txt";
        //int M = 3;
        //ArcadeMachine.runGames(game, new String[]{level1, level2}, M, sampleRandomController, null, seed);

        //5. This plays N games, in the first L levels, M times each. Actions to file optional (set saveActions to true).
//        int N = 10, L = 5, M = 2;
//        boolean saveActions = false;
//        String[] levels = new String[L];
//        String[] actionFiles = new String[L*M];
//        for(int i = 0; i < N; ++i)
//        {
//            int actionIdx = 0;
//            game = gamesPath + games[i] + ".txt";
//            for(int j = 0; j < L; ++j){
//                levels[j] = gamesPath + games[i] + "_lvl" + j +".txt";
//                if(saveActions) for(int k = 0; k < M; ++k)
//                    actionFiles[actionIdx++] = "actions_game_" + i + "_level_" + j + "_" + k + ".txt";
//            }
//            ArcadeMachine.runGames(game, levels, M, sampleOLMCTSController, saveActions? actionFiles:null, seed);
//        }

        //2014_12_12 FRI: for the number of repeats play all games defined in games[], all 5 levels for each
        FRItesting(gamesPath, seed);
    }

    public static void FRItesting(String gamesPath, int seed)
    {
        //---- config section ----//

        //-- experimental settings
        //String gameSet[] = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs", "missilecommand", "portals", "sokoban", "survivezombies", "zelda"};   //training CIG2014
        String gameSet[] = new String[]{"camelRace", "digdug", "firestorms", "infection", "firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania"};   //validation CIG2014
        //String gameSet[] = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs", "missilecommand", "portals", "sokoban", "survivezombies", "zelda","camelRace", "digdug", "firestorms", "infection", "firecaster", "overload", "pacman", "seaquest", "whackamole", "eggomania"};   //training+validation CIG2014

        //String controller = "controllers.sampleMCTS.Agent";
        String controller = "controllers.sampleOLMCTS.Agent";

        int numLevelsPerGame = 1;
        int repeats = 100;

        //-- output printing settings
        int stdOutDepth = 0;   //valid values from 0 to 2
        int stdOutDetail = 1;  //valid values from 0 to 2
        boolean printToFiles = true;

        //-- base name of output file(s)
        String wkDir = System.getProperty("user.dir");
        String dateText = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String outputFilename = wkDir.substring(wkDir.lastIndexOf("\\")+1)+"__"+dateText;
        String filenameEnding = ".txt";

        //---- END OF CONFIG SECTION ----//
        // do not change code below here unless you know what you're doing

        // initialize structures and files for gathering experimental results
        int numGames = gameSet.length;
        Metrics.initStats(numGames, numLevelsPerGame);
        Metrics.printIgnoreMetrics[Metrics.TIME_INIT] = true;  //example how to disable the output of a specific metric, must be set after .initStats()
        if(printToFiles)
            Metrics.initFiles(outputFilename, filenameEnding);

        // print configuration and stat headers
        Metrics.printConfiguration(gameSet, numLevelsPerGame, controller, printToFiles);
        Metrics.printHeader(stdOutDepth,stdOutDetail,"                    "," rep   g l          ", printToFiles);

        // run experiments
        ArcadeMachine.runGamesFRI(repeats, gamesPath, gameSet, numLevelsPerGame, controller, seed, stdOutDepth, stdOutDetail, printToFiles);

        // close files
        if(printToFiles)
            Metrics.closeFiles();
    }

}
