package com.zoovisitors.bl;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfoResult;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Memory {

    private Enclosure[] enclosures;
    private Animal.PersonalStories[] animalStories;
    private Misc[] miscMarkers;
    private MapResult mapResult;
    private WallFeed[] wallFeeds;
    private ContactInfoResult contactInfoResult;
    private OpeningHoursResult openingHoursResult;
    private Price[] prices;
    private String aboutUs;

    public Memory(Enclosure[] enclosures, Animal.PersonalStories[] animalStories,
                  Misc[] miscMarkers, MapResult mapResult, WallFeed[] wallFeeds,
                  ContactInfoResult contactInfoResult, OpeningHoursResult openingHoursResult,
                  Price[] prices, String aboutUs) {
        this.enclosures = enclosures;
        this.animalStories = animalStories;
        this.miscMarkers = miscMarkers;
        this.mapResult = mapResult;
        this.wallFeeds = wallFeeds;
        this.contactInfoResult = contactInfoResult;
        this.openingHoursResult = openingHoursResult;
        this.prices = prices;
        this.aboutUs = aboutUs;
        this.stringToDrawableMap = new HashMap<>();
    }

    //    static {
//        for (Point point:
//        points) {
//            routes.put(point, new HashSet<>());
//            minX = Math.min(minX, point.getX());
//            maxX = Math.max(maxX, point.getX());
//            minY = Math.min(minY, point.getY());
//            maxY = Math.max(maxY, point.getY());
//        }
//    }

    public static Map<String, Bitmap> urlToBitmapMap = new HashMap<String, Bitmap>();
    private Map<String, Drawable> stringToDrawableMap;

    public void setStringAndDrawable(String s, Drawable d){
        stringToDrawableMap.put(s,d);
    }

    public Drawable getDrawableByString(String s){
        return stringToDrawableMap.get(s);
    }

    private static Location[] locations = {
            new Location(31.25806349,34.74502552),
            new Location(31.25810072,34.74500303),
            new Location(31.25813795,34.74498055),
            new Location(31.25817518,34.74495806),
            new Location(31.25821241,34.74493557),
            new Location(31.25824964,34.74491308),
            new Location(31.25828687,34.7448906),
            new Location(31.2583241,34.74486811),
            new Location(31.25836133,34.74484562),
            new Location(31.25822916,34.74490333),
            new Location(31.25821407,34.74487064),
            new Location(31.25819899,34.74484852),
            new Location(31.25818391,34.74483696),
            new Location(31.25816883,34.74483597),
            new Location(31.25836118,34.744838),
            new Location(31.25834537,34.74478725),
            new Location(31.25832812,34.7447365),
            new Location(31.25831316,34.74468575),
            new Location(31.25830319,34.744635),
            new Location(31.2582999,34.74458425),
            new Location(31.25830399,34.7445335),
            new Location(31.25831517,34.74448275),
            new Location(31.25833213,34.744432),
            new Location(31.25835256,34.74438125),
            new Location(31.25837315,34.74433050),
            new Location(31.25837915,34.7442945),
            new Location(31.25824825,34.74451287),
            new Location(31.25826147,34.74453149),
            new Location(31.2582747,34.74454419),
            new Location(31.25828793,34.74455099),
            new Location(31.25830116,34.74455187),
            new Location(31.25826466,34.74436922),
            new Location(31.25828257,34.74438196),
            new Location(31.25830049,34.74439469),
            new Location(31.25831841,34.74440742),
            new Location(31.25833633,34.74442016),
            new Location(31.25826366,34.74392066),
            new Location(31.25823983,34.74393641),
            new Location(31.25821974,34.74395816),
            new Location(31.25820637,34.74397991),
            new Location(31.2581988,34.74400166),
            new Location(31.25819622,34.74402341),
            new Location(31.25819794,34.74404516),
            new Location(31.25820336,34.74406691),
            new Location(31.258212,34.74408866),
            new Location(31.25822351,34.74411041),
            new Location(31.2582376,34.74413216),
            new Location(31.25825414,34.74415391),
            new Location(31.25827309,34.74417566),
            new Location(31.2582945,34.74419741),
            new Location(31.25831857,34.74421916),
            new Location(31.25834557,34.74424091),
            new Location(31.25837591,34.74426266),
            new Location(31.25825333,34.74432123),
            new Location(31.25826528,34.7442927),
            new Location(31.25827724,34.74426416),
            new Location(31.2582892,34.74423563),
            new Location(31.25830116,34.74420710),
            new Location(31.2582877,34.74391367),
            new Location(31.25830642,34.74391211),
            new Location(31.25832513,34.74391276),
            new Location(31.25834385,34.74391552),
            new Location(31.25836257,34.7439203),
            new Location(31.25838128,34.743927),
            new Location(31.2584,34.74393553),
            new Location(31.25841872,34.74394582),
            new Location(31.25843743,34.74395777),
            new Location(31.25845615,34.74397131),
            new Location(31.25847486,34.74398637),
            new Location(31.25849358,34.74400286),
            new Location(31.2585123,34.74402073),
            new Location(31.25853101,34.74403990),
            new Location(31.25854973,34.74406032),
            new Location(31.25856845,34.74408191),
            new Location(31.25858716,34.74410464),
            new Location(31.25860588,34.74412844),
            new Location(31.25862459,34.74415326),
            new Location(31.25864331,34.74417906),
            new Location(31.25866203,34.7442058),
            new Location(31.25868074,34.74423342),
            new Location(31.25869946,34.74426191),
            new Location(31.25871818,34.74429122),
            new Location(31.25873689,34.74432133),
            new Location(31.25875561,34.7443522),
            new Location(31.25877433,34.74438381),
            new Location(31.2583705,34.74389652),
            new Location(31.25838489,34.74386665),
            new Location(31.25839929,34.74383898),
            new Location(31.25841369,34.74381356),
            new Location(31.25842808,34.74379045),
            new Location(31.25844248,34.74376967),
            new Location(31.25845688,34.74375123),
            new Location(31.25847128,34.74373513),
            new Location(31.25848567,34.74372131),
            new Location(31.25850007,34.74370974),
            new Location(31.25851447,34.74370033),
            new Location(31.25852887,34.74369299),
            new Location(31.25854326,34.74368759),
            new Location(31.25855766,34.743684),
            new Location(31.25857206,34.74368205),
            new Location(31.25858645,34.74368156),
            new Location(31.25860085,34.74368234),
            new Location(31.25861525,34.74368415),
            new Location(31.25862965,34.74368675),
            new Location(31.25864404,34.74368987),
            new Location(31.25865844,34.74369323),
            new Location(31.25867284,34.74369652),
            new Location(31.25868724,34.74369940),
            new Location(31.25870163,34.74370153),
            new Location(31.25871603,34.74370254),
            new Location(31.25873043,34.74370203),
            new Location(31.25874483,34.74369958),
            new Location(31.25846983,34.74395682),
            new Location(31.25848701,34.74392677),
            new Location(31.2585042,34.74389108),
            new Location(31.25852139,34.74385576),
            new Location(31.25853858,34.74382468),
            new Location(31.25855576,34.74379988),
            new Location(31.25857295,34.74378191),
            new Location(31.25859014,34.74377016),
            new Location(31.25860733,34.74376320),
            new Location(31.25862451,34.74375909),
            new Location(31.2586417,34.74375575),
            new Location(31.25865889,34.74375124),
            new Location(31.25867608,34.74374417),
            new Location(31.25869326,34.74373395),
            new Location(31.25871045,34.74372118),
            new Location(31.25872764,34.74370796),
            new Location(31.258468,34.74376126),
            new Location(31.2584832,34.74377182),
            new Location(31.25849841,34.74378237),
            new Location(31.25851362,34.74379292),
            new Location(31.25852883,34.74380347),
            new Location(31.2586000000,34.7437526667),
            new Location(31.2585831667,34.7437061667),
            new Location(31.25859158335,34.7437294167),
            new Location(31.25861618,34.74372462),
            new Location(31.25864079,34.74371983),
            new Location(31.25866539,34.74371504),
            new Location(31.25877672,34.74369607),
            new Location(31.25880461,34.74369356),
            new Location(31.25883456,34.74369844),
            new Location(31.25886051,34.74369994),
            new Location(31.25888646,34.74369851),
            new Location(31.25891241,34.74369541),
            new Location(31.25893836,34.74369167),
            new Location(31.25896431,34.74368816),
            new Location(31.25899026,34.74368555),
            new Location(31.25901621,34.74368439),
            new Location(31.25904216,34.74368504),
            new Location(31.25906811,34.74368778),
            new Location(31.25909406,34.74369273),
            new Location(31.25912001,34.74369993),
            new Location(31.25914596,34.74370935),
            new Location(31.25917191,34.74372085),
            new Location(31.25919786,34.74373426),
            new Location(31.25922381,34.74374935),
            new Location(31.25924976,34.74376588),
            new Location(31.25927572,34.74378358),
            new Location(31.25930167,34.74380217),
            new Location(31.25932762,34.74382141),
            new Location(31.25935357,34.74384105),
            new Location(31.25937952,34.74386091),
            new Location(31.25940547,34.74388087),
            new Location(31.25943142,34.74390085),
            new Location(31.25945737,34.74392087),
            new Location(31.25948332,34.74394107),
            new Location(31.25950927,34.74396166),
            new Location(31.25953522,34.74398301),
            new Location(31.25956117,34.74400561),
            new Location(31.25958712,34.74403012),
            new Location(31.25961307,34.74405736),
            new Location(31.25963902,34.74408834),
            new Location(31.25966497,34.74412427),
            new Location(31.25969092,34.74416655),
            new Location(31.25971687,34.74421684),
            new Location(31.25974283,34.74427701),
            new Location(31.25883528,34.74368602),
            new Location(31.25883601,34.74367361),
            new Location(31.2592825,34.74378816),
            new Location(31.25929883,34.74376162),
            new Location(31.25931516,34.74373508),
            new Location(31.25933149,34.74370854),
            new Location(31.25923799,34.74360382),
            new Location(31.25926915,34.74362634),
            new Location(31.25930032,34.74364887),
            new Location(31.25933149,34.74367139),
            new Location(31.25918843,34.74369519),
            new Location(31.25920495,34.74366491),
            new Location(31.25922147,34.74363464),
            new Location(31.25955258,34.74395782),
            new Location(31.25956994,34.74392794),
            new Location(31.2595873,34.74389806),
            new Location(31.25960466,34.74386817),
            new Location(31.25964233,34.74439113),
            new Location(31.25964842,34.74435464),
            new Location(31.25965451,34.74431814),
            new Location(31.25966061,34.74428165),
            new Location(31.2596667,34.74424515),
            new Location(31.2596728,34.74420865),
            new Location(31.259655,34.74441895),
            new Location(31.25968009,34.74446299),
            new Location(31.25970518,34.74448928),
            new Location(31.25973028,34.74450113),
            new Location(31.25975537,34.74450189),
            new Location(31.25978047,34.74449486),
            new Location(31.25980556,34.74448337),
            new Location(31.259829,34.744465),
            new Location(31.25984715,34.74434716),
            new Location(31.25984826,34.74437811),
            new Location(31.25984588,34.74440907),
            new Location(31.25984001,34.74444003),
            new Location(31.25973099,34.74440629),
            new Location(31.2597426,34.74434355),
            new Location(31.25975422,34.74430801),
            new Location(31.25976584,34.7442901),
            new Location(31.25977745,34.74428257),
            new Location(31.25978907,34.74428055),
            new Location(31.25980069,34.74428152),
            new Location(31.2598123,34.74428531),
            new Location(31.25982392,34.74429410),
            new Location(31.25983554,34.74431245),
            new Location(31.259843,34.7443297),
            new Location(31.25949816,34.74476710),
            new Location(31.25951041,34.74475197),
            new Location(31.25952266,34.74473564),
            new Location(31.25953492,34.74471853),
            new Location(31.25954717,34.74470099),
            new Location(31.25955943,34.74468331),
            new Location(31.25957168,34.74466569),
            new Location(31.25958393,34.74464828),
            new Location(31.25959619,34.74463115),
            new Location(31.25960844,34.74461428),
            new Location(31.2596207,34.74459759),
            new Location(31.25963295,34.74458096),
            new Location(31.25964521,34.74456414),
            new Location(31.25965746,34.74454685),
            new Location(31.25966971,34.74452872),
            new Location(31.25968197,34.74450932),
            new Location(31.25969422,34.74448814),
            new Location(31.25970648,34.74446461),
            new Location(31.25971873,34.74443806),
            new Location(31.259736795,34.74437566),
            new Location(31.25933699,34.74479599),
            new Location(31.25936385,34.74481858),
            new Location(31.25939071,34.74483093),
            new Location(31.25941757,34.7448325),
            new Location(31.25944443,34.74482275),
            new Location(31.25947129,34.74480115),
            new Location(31.259484725,34.744784155),
            new Location(31.25923833,34.7447195),
            new Location(31.25927121,34.74474829),
            new Location(31.2593041,34.74476769),
            new Location(31.25913999,34.74484471),
            new Location(31.25916411,34.74481366),
            new Location(31.25918824,34.74477414),
            new Location(31.25921237,34.74475187),
            new Location(31.2591964,34.74482281),
            new Location(31.25921532,34.74485681),
            new Location(31.25923424,34.7448826),
            new Location(31.25925316,34.74490196),
            new Location(31.25927208,34.74492246),
            new Location(31.259291,34.74495749),
            new Location(31.2592655000,34.7449738333),
            new Location(31.2592400000,34.7449335),
            new Location(31.25914128,34.74477521),
            new Location(31.25913531,34.74480159),
            new Location(31.25913754,34.74482797),
            new Location(31.25914092,34.74485435),
            new Location(31.25914231,34.74488073),
            new Location(31.25915814,34.74490711),
            new Location(31.2592088133,34.7449347033),
            new Location(31.2591805266,34.7449287066),
            new Location(31.25926222,34.74429133),
            new Location(31.25928167,34.74431824),
            new Location(31.25929937,34.74434515),
            new Location(31.25931459,34.74437206),
            new Location(31.25932672,34.74439897),
            new Location(31.2593353,34.74442588),
            new Location(31.25933995,34.7444528),
            new Location(31.25934044,34.74447971),
            new Location(31.25933666,34.74450662),
            new Location(31.25932862,34.74453353),
            new Location(31.25931644,34.74456044),
            new Location(31.25930039,34.74458735),
            new Location(31.25928084,34.74461427),
            new Location(31.25925829,34.74464118),
            new Location(31.25923337,34.74466809),
            new Location(31.25920683,34.744695),
            new Location(31.25917953,34.74472191),
            new Location(31.25915247,34.74474883),
            new Location(31.25924582,34.74419966),
            new Location(31.25924651,34.74422816),
            new Location(31.25924907,34.74425666),
            new Location(31.25917099,34.74420782),
            new Location(31.2591822,34.74418078),
            new Location(31.25920341,34.74416791),
            new Location(31.25922962,34.7441742),
            new Location(31.25917099,34.74425283),
            new Location(31.2591862,34.74428952),
            new Location(31.25921141,34.74430803),
            new Location(31.25923662,34.74429856),
            new Location(31.25917916,34.74439466),
            new Location(31.25920903,34.74434924),
            new Location(31.25923891,34.74433677),
            new Location(31.25926878,34.74434007),
            new Location(31.25880281,34.74483935),
            new Location(31.25882333,34.74481831),
            new Location(31.25884385,34.74479727),
            new Location(31.25886438,34.7447737),
            new Location(31.2588849,34.7447484),
            new Location(31.25890543,34.74472207),
            new Location(31.25892596,34.74469528),
            new Location(31.25894648,34.74466851),
            new Location(31.25896701,34.74464215),
            new Location(31.25898754,34.74461644),
            new Location(31.25900806,34.74459155),
            new Location(31.25902859,34.74456752),
            new Location(31.25904911,34.7445443),
            new Location(31.25906964,34.74452171),
            new Location(31.25909017,34.74449949),
            new Location(31.25911069,34.74447726),
            new Location(31.25913122,34.74445451),
            new Location(31.25915175,34.74443067),
            new Location(31.25917227,34.74440502),
            new Location(31.2591928,34.74437675),
            new Location(31.25872229,34.74474383),
            new Location(31.25873753,34.74478023),
            new Location(31.25875405,34.74481664),
            new Location(31.25877493,34.74485305),
            new Location(31.25880116,34.74488945),
            new Location(31.2588316,34.74492586),
            new Location(31.25886304,34.74496227),
            new Location(31.25889013,34.74499867),
            new Location(31.25890544,34.74503508),
            new Location(31.25889942,34.74507149),
            new Location(31.25879177,34.744415035),
            new Location(31.25880921,34.74444626),
            new Location(31.2588167,34.74446287),
            new Location(31.2588172,34.74447948),
            new Location(31.25881354,34.74449608),
            new Location(31.25880778,34.74451269),
            new Location(31.25880131,34.7445293),
            new Location(31.25879501,34.7445459),
            new Location(31.25878927,34.74456251),
            new Location(31.25878418,34.74457912),
            new Location(31.2587795,34.74459572),
            new Location(31.25877485,34.74461233),
            new Location(31.2587697,34.74462894),
            new Location(31.25876347,34.74464555),
            new Location(31.25875556,34.74466215),
            new Location(31.25874544,34.74467876),
            new Location(31.25873263,34.74469537),
            new Location(31.25871678,34.74471197),
            new Location(31.25869766,34.74472858),
            new Location(31.2586752,34.74474519),
            new Location(31.2586495,34.74476179),
            new Location(31.2586208,34.7447784),
            new Location(31.2585895,34.74479501),
            new Location(31.25855614,34.74481161),
            new Location(31.25852138,34.74482822),
            new Location(31.25848596,34.74484483),
            new Location(31.25845067,34.74486144),
            new Location(31.25841631,34.74487804),
            new Location(31.25838363,34.74489465),
            new Location(31.25835327,34.74491126),
            new Location(31.25832568,34.74492786),
            new Location(31.25830107,34.74494447),
            new Location(31.25827932,34.74496108),
            new Location(31.25825987,34.74497768),
            new Location(31.25824164,34.74499429),
            new Location(31.25822292,34.7450109),
            new Location(31.25820124,34.7450275),
            new Location(31.25817328,34.74504411),
            new Location(31.25813471,34.74506072),
            new Location(31.25823446,34.74500433),
            new Location(31.25824774,34.7450474),
            new Location(31.25826408,34.74509047),
            new Location(31.25828511,34.74513355),
            new Location(31.25830841,34.74517662),
            new Location(31.25832971,34.7452197),
            new Location(31.25834512,34.74526277),
            new Location(31.25835328,34.74530585),
            new Location(31.25835761,34.74534892),
            new Location(31.25836848,34.745392),
            new Location(31.25839021,34.74542182),
            new Location(31.25841616,34.74545349),
            new Location(31.25844195,34.74548515),
            new Location(31.25846431,34.74551682),
            new Location(31.25847997,34.74554849),
            new Location(31.2585575,34.74542683),
            new Location(31.25855656,34.74545285),
            new Location(31.25855032,34.74547888),
            new Location(31.25853878,34.74550491),
            new Location(31.25852194,34.74553094),
            new Location(31.2584998,34.74555697),
            new Location(31.25847235,34.745583),
            new Location(31.25841133,34.74543557),
            new Location(31.25843574,34.74541561),
            new Location(31.25846016,34.74539814),
            new Location(31.25848458,34.74538608),
            new Location(31.25850899,34.74538336),
            new Location(31.25853341,34.74539501),
            new Location(31.25854562,34.74541106),
            new Location(31.25846849,34.74540686),
            new Location(31.25848466,34.74542226),
            new Location(31.25850083,34.74543911),
            new Location(31.25851699,34.7454574),
            new Location(31.25853316,34.74547714),
            new Location(31.2583145,34.74516825),
            new Location(31.25833379,34.74517837),
            new Location(31.25835308,34.74518658),
            new Location(31.25837237,34.74519337),
            new Location(31.25839166,34.74519918),
            new Location(31.25841095,34.74520441),
            new Location(31.25843024,34.74520943),
            new Location(31.25844954,34.74521454),
            new Location(31.25846883,34.74522002),
            new Location(31.25848812,34.7452261),
            new Location(31.25850741,34.74523297),
            new Location(31.2585267,34.74524078),
            new Location(31.25854599,34.74524962),
            new Location(31.25856528,34.74525955),
            new Location(31.25858458,34.7452706),
            new Location(31.25860387,34.74528274),
            new Location(31.25862316,34.74529589),
            new Location(31.25864245,34.74530995),
            new Location(31.25866174,34.74532475),
            new Location(31.25868103,34.74534012),
            new Location(31.25870033,34.7453558),
            new Location(31.25870032,34.74535579),
            new Location(31.25871276,34.74533802),
            new Location(31.2587252,34.74532025),
            new Location(31.25873764,34.74530248),
            new Location(31.25875009,34.74528471),
            new Location(31.25876253,34.74526694),
            new Location(31.25877497,34.74524917),
            new Location(31.25878742,34.7452314),
            new Location(31.25879986,34.74521363),
            new Location(31.2588123,34.74519586),
            new Location(31.25882475,34.74517809),
            new Location(31.25883719,34.74516032),
            new Location(31.25884963,34.74514255),
            new Location(31.25886208,34.74512478),
            new Location(31.25887452,34.74510701),
            new Location(31.25888696,34.74508924),
            new Location(31.25889941,34.74507148),
            new Location(31.25808008,34.74507733)
    };
//        private static Point[] points = {
//                new Point(94, 606),
//                new Point(111, 579),
//                new Point(117, 621),
//                new Point(128, 552),
//                new Point(140, 635),
//                new Point(145, 525),
//                new Point(146, 957),
//                new Point(152, 1174),
//                new Point(153, 1187),
//                new Point(153, 1162),
//                new Point(154, 1200),
//                new Point(154, 1149),
//                new Point(154, 800),
//                new Point(155, 822),
//                new Point(155, 777),
//                new Point(156, 1137),
//                new Point(156, 957),
//                new Point(157, 1212),
//                new Point(157, 845),
//                new Point(157, 754),
//                new Point(159, 1124),
//                new Point(160, 868),
//                new Point(161, 1225),
//                new Point(161, 1112),
//                new Point(161, 984),
//                new Point(161, 732),
//                new Point(163, 1009),
//                new Point(163, 890),
//                new Point(163, 649),
//                new Point(164, 1099),
//                new Point(165, 958),
//                new Point(165, 913),
//                new Point(166, 1237),
//                new Point(166, 1087),
//                new Point(166, 1036),
//                new Point(166, 935),
//                new Point(166, 709),
//                new Point(167, 1074),
//                new Point(168, 1061),
//                new Point(168, 1049),
//                new Point(171, 1177),
//                new Point(172, 1051),
//                new Point(172, 525),
//                new Point(173, 1250),
//                new Point(173, 687),
//                new Point(177, 1106),
//                new Point(181, 1127),
//                new Point(182, 1262),
//                new Point(182, 1066),
//                new Point(182, 664),
//                new Point(184, 1149),
//                new Point(188, 1170),
//                new Point(191, 1081),
//                new Point(192, 1275),
//                new Point(192, 641),
//                new Point(192, 539),
//                new Point(199, 1096),
//                new Point(203, 619),
//                new Point(204, 1287),
//                new Point(204, 1111),
//                new Point(205, 1163),
//                new Point(208, 1126),
//                new Point(210, 1141),
//                new Point(212, 1278),
//                new Point(212, 553),
//                new Point(213, 1156),
//                new Point(215, 596),
//                new Point(218, 1300),
//                new Point(218, 1171),
//                new Point(220, 1264),
//                new Point(227, 1186),
//                new Point(228, 1251),
//                new Point(228, 573),
//                new Point(232, 567),
//                new Point(234, 1313),
//                new Point(235, 1238),
//                new Point(241, 1201),
//                new Point(242, 551),
//                new Point(243, 1225),
//                new Point(251, 1325),
//                new Point(257, 528),
//                new Point(259, 1216),
//                new Point(270, 1338),
//                new Point(271, 505),
//                new Point(282, 1231),
//                new Point(286, 483),
//                new Point(290, 1350),
//                new Point(291, 286),
//                new Point(301, 460),
//                new Point(309, 1246),
//                new Point(313, 1363),
//                new Point(314, 302),
//                new Point(316, 438),
//                new Point(324, 1419),
//                new Point(325, 1435),
//                new Point(325, 1402),
//                new Point(327, 1386),
//                new Point(330, 1370),
//                new Point(331, 1456),
//                new Point(331, 415),
//                new Point(335, 1353),
//                new Point(335, 1261),
//                new Point(336, 317),
//                new Point(342, 1477),
//                new Point(342, 1337),
//                new Point(346, 392),
//                new Point(349, 1321),
//                new Point(358, 1304),
//                new Point(358, 1276),
//                new Point(358, 332),
//                new Point(359, 1494),
//                new Point(361, 370),
//                new Point(368, 1288),
//                new Point(375, 1506),
//                new Point(377, 347),
//                new Point(380, 1272),
//                new Point(391, 1513),
//                new Point(392, 1255),
//                new Point(394, 324),
//                new Point(405, 1239),
//                new Point(407, 1515),
//                new Point(412, 302),
//                new Point(420, 1223),
//                new Point(423, 1513),
//                new Point(433, 279),
//                new Point(435, 1206),
//                new Point(440, 1509),
//                new Point(451, 1190),
//                new Point(456, 1501),
//                new Point(456, 257),
//                new Point(468, 1174),
//                new Point(472, 1491),
//                new Point(482, 234),
//                new Point(486, 1158),
//                new Point(488, 1479),
//                new Point(504, 1141),
//                new Point(505, 1464),
//                new Point(514, 211),
//                new Point(515, 636),
//                new Point(520, 614),
//                new Point(521, 1448),
//                new Point(523, 1125),
//                new Point(525, 655),
//                new Point(537, 1429),
//                new Point(539, 599),
//                new Point(543, 1109),
//                new Point(544, 1423),
//                new Point(545, 227),
//                new Point(545, 665),
//                new Point(551, 189),
//                new Point(553, 1408),
//                new Point(560, 599),
//                new Point(564, 1092),
//                new Point(566, 1434),
//                new Point(569, 1385),
//                new Point(573, 232),
//                new Point(578, 665),
//                new Point(581, 597),
//                new Point(585, 1076),
//                new Point(586, 1358),
//                new Point(587, 1444),
//                new Point(596, 166),
//                new Point(599, 126),
//                new Point(600, 238),
//                new Point(600, 116),
//                new Point(601, 136),
//                new Point(603, 105),
//                new Point(606, 146),
//                new Point(606, 651),
//                new Point(607, 1060),
//                new Point(607, 585),
//                new Point(608, 1455),
//                new Point(609, 1355),
//                new Point(609, 95),
//                new Point(612, 607),
//                new Point(619, 156),
//                new Point(619, 629),
//                new Point(623, 85),
//                new Point(627, 243),
//                new Point(627, 568),
//                new Point(629, 1465),
//                new Point(629, 1043),
//                new Point(636, 1360),
//                new Point(636, 79),
//                new Point(641, 605),
//                new Point(643, 579),
//                new Point(646, 166),
//                new Point(647, 553),
//                new Point(649, 75),
//                new Point(650, 632),
//                new Point(652, 1027),
//                new Point(654, 248),
//                new Point(665, 1455),
//                new Point(667, 539),
//                new Point(670, 171),
//                new Point(671, 646),
//                new Point(672, 74),
//                new Point(674, 1378),
//                new Point(675, 1439),
//                new Point(676, 1011),
//                new Point(681, 254),
//                new Point(684, 1424),
//                new Point(684, 658),
//                new Point(687, 529),
//                new Point(692, 664),
//                new Point(693, 176),
//                new Point(694, 1408),
//                new Point(695, 76),
//                new Point(699, 995),
//                new Point(702, 243),
//                new Point(703, 1393),
//                new Point(707, 521),
//                new Point(711, 681),
//                new Point(712, 1396),
//                new Point(716, 187),
//                new Point(718, 81),
//                new Point(723, 980),
//                new Point(727, 517),
//                new Point(729, 699),
//                new Point(735, 221),
//                new Point(735, 974),
//                new Point(736, 198),
//                new Point(737, 91),
//                new Point(746, 717),
//                new Point(747, 973),
//                new Point(748, 517),
//                new Point(750, 1411),
//                new Point(750, 111),
//                new Point(754, 208),
//                new Point(755, 199),
//                new Point(759, 133),
//                new Point(760, 976),
//                new Point(762, 735),
//                new Point(763, 177),
//                new Point(764, 155),
//                new Point(768, 520),
//                new Point(770, 219),
//                new Point(772, 1469),
//                new Point(772, 981),
//                new Point(779, 753),
//                new Point(784, 230),
//                new Point(784, 987),
//                new Point(786, 1458),
//                new Point(788, 1421),
//                new Point(788, 527),
//                new Point(796, 1446),
//                new Point(796, 771),
//                new Point(797, 993),
//                new Point(798, 240),
//                new Point(801, 1435),
//                new Point(801, 1423),
//                new Point(808, 538),
//                new Point(809, 998),
//                new Point(810, 251),
//                new Point(813, 789),
//                new Point(822, 1002),
//                new Point(823, 262),
//                new Point(825, 1424),
//                new Point(828, 552),
//                new Point(831, 807),
//                new Point(834, 1006),
//                new Point(835, 273),
//                new Point(846, 1010),
//                new Point(848, 283),
//                new Point(848, 569),
//                new Point(849, 825),
//                new Point(859, 1015),
//                new Point(860, 294),
//                new Point(863, 1421),
//                new Point(868, 589),
//                new Point(869, 843),
//                new Point(871, 1020),
//                new Point(873, 305),
//                new Point(884, 1027),
//                new Point(886, 315),
//                new Point(888, 610),
//                new Point(888, 860),
//                new Point(896, 1036),
//                new Point(899, 326),
//                new Point(901, 1413),
//                new Point(908, 633),
//                new Point(908, 878),
//                new Point(908, 1047),
//                new Point(913, 337),
//                new Point(921, 1061),
//                new Point(926, 347),
//                new Point(926, 606),
//                new Point(928, 657),
//                new Point(928, 896),
//                new Point(933, 1077),
//                new Point(938, 358),
//                new Point(939, 1400),
//                new Point(944, 1056),
//                new Point(945, 1097),
//                new Point(948, 577),
//                new Point(948, 681),
//                new Point(948, 914),
//                new Point(950, 629),
//                new Point(951, 369),
//                new Point(958, 1119),
//                new Point(962, 379),
//                new Point(962, 549),
//                new Point(967, 650),
//                new Point(967, 932),
//                new Point(968, 691),
//                new Point(970, 1144),
//                new Point(972, 1043),
//                new Point(975, 391),
//                new Point(977, 1385),
//                new Point(983, 520),
//                new Point(983, 1172),
//                new Point(984, 950),
//                new Point(987, 403),
//                new Point(988, 696),
//                new Point(995, 1201),
//                new Point(997, 671),
//                new Point(999, 1028),
//                new Point(1000, 497),
//                new Point(1000, 968),
//                new Point(1003, 426),
//                new Point(1003, 643),
//                new Point(1007, 694),
//                new Point(1007, 1231),
//                new Point(1009, 473),
//                new Point(1011, 450),
//                new Point(1013, 1539),
//                new Point(1014, 1525),
//                new Point(1015, 1371),
//                new Point(1016, 986),
//                new Point(1020, 1371),
//                new Point(1020, 692),
//                new Point(1020, 1262),
//                new Point(1023, 1512),
//                new Point(1026, 1010),
//                new Point(1027, 691),
//                new Point(1029, 626),
//                new Point(1032, 1293),
//                new Point(1037, 1403),
//                new Point(1039, 1499),
//                new Point(1045, 1323),
//                new Point(1047, 690),
//                new Point(1048, 610),
//                new Point(1053, 987),
//                new Point(1054, 1436),
//                new Point(1057, 1351),
//                new Point(1062, 593),
//                new Point(1063, 1486),
//                new Point(1066, 676),
//                new Point(1069, 1378),
//                new Point(1071, 1468),
//                new Point(1078, 577),
//                new Point(1080, 961),
//                new Point(1082, 656),
//                new Point(1082, 1402),
//                new Point(1086, 605),
//                new Point(1087, 1501),
//                new Point(1087, 632),
//                new Point(1094, 1423),
//                new Point(1104, 1533),
//                new Point(1104, 560),
//                new Point(1106, 1442),
//                new Point(1107, 933),
//                new Point(1116, 582),
//                new Point(1119, 1459),
//                new Point(1121, 1566),
//                new Point(1131, 1475),
//                new Point(1134, 910),
//                new Point(1138, 1598),
//                new Point(1139, 1481),
//                new Point(1144, 1491),
//                new Point(1154, 1630),
//                new Point(1155, 1510),
//                new Point(1161, 896),
//                new Point(1167, 1535),
//                new Point(1170, 1470),
//                new Point(1180, 1568),
//                new Point(1188, 902),
//                new Point(1188, 902),
//                new Point(1192, 1616),
//                new Point(1201, 912),
//                new Point(1202, 1456),
//                new Point(1214, 923),
//                new Point(1228, 934),
//                new Point(1234, 1437),
//                new Point(1241, 945),
//                new Point(1254, 956),
//                new Point(1260, 1412),
//                new Point(1266, 1417),
//                new Point(1267, 1395),
//                new Point(1267, 967),
//                new Point(1274, 1378),
//                new Point(1279, 1361),
//                new Point(1281, 977),
//                new Point(1283, 1344),
//                new Point(1287, 1327),
//                new Point(1291, 1311),
//                new Point(1294, 1294),
//                new Point(1294, 988),
//                new Point(1298, 1398),
//                new Point(1299, 1277),
//                new Point(1303, 1260),
//                new Point(1307, 999),
//                new Point(1308, 1243),
//                new Point(1314, 1227),
//                new Point(1320, 1010),
//                new Point(1321, 1210),
//                new Point(1328, 1193),
//                new Point(1330, 1385),
//                new Point(1334, 1021),
//                new Point(1336, 1176),
//                new Point(1345, 1159),
//                new Point(1347, 1032),
//                new Point(1355, 1142),
//                new Point(1360, 1043),
//                new Point(1363, 1378),
//                new Point(1366, 1126),
//                new Point(1373, 1053),
//                new Point(1377, 1109),
//                new Point(1387, 1064),
//                new Point(1388, 1092),
//                new Point(1395, 1374),
//                new Point(1400, 1075),
//                new Point(1400, 1075),
//                new Point(1420, 1242),
//                new Point(1422, 1263),
//                new Point(1427, 1365),
//                new Point(1429, 1221),
//                new Point(1431, 1285),
//                new Point(1438, 1277),
//                new Point(1441, 1210),
//                new Point(1444, 1306),
//                new Point(1449, 1346),
//                new Point(1449, 1263),
//                new Point(1453, 1200),
//                new Point(1459, 1327),
//                new Point(1462, 1249),
//                new Point(1472, 1201),
//                new Point(1473, 1323),
//                new Point(1476, 1235),
//                new Point(1490, 1221),
//                new Point(1492, 1206),
//                new Point(1496, 1300),
//                new Point(1511, 1216),
//                new Point(1520, 1281),
//                new Point(1530, 1231),
//                new Point(1543, 1267),
//                new Point(1550, 1250),
//                new Point(1569, 1274)
//        };


    public static Location ZOO_ENTRANCE_LOCATION = new Location(31.25806349, 34.74502552);
    public static Point ZOO_ENTRANCE_POINT = new Point(1088, 1494);                              // 1.5573580533024333719582850521437
    private static Location ZOO_EXIT_LOCATION = new Location(31.25960466,34.74386817); //-0.00154117
    private static Point ZOO_EXIT_POINT = new Point(335, 322);
    // the borders of the zoo
//    public static double minLongitude = Double.MAX_VALUE;
//    public static double maxLongitude = Double.MIN_VALUE;
//    public static double minLatitude = Double.MAX_VALUE;
//    public static double maxLatitude = Double.MIN_VALUE;
    // TODO: get the true values
    public static double minLongitude = Double.MIN_VALUE;
    public static double maxLongitude = Double.MAX_VALUE;
    public static double minLatitude = Double.MIN_VALUE;
    public static double maxLatitude = Double.MAX_VALUE;

    private static double getAlpha(){
        return Math.abs(Math.atan2(ZOO_ENTRANCE_POINT.getY() - ZOO_EXIT_POINT.getY(),
                ZOO_ENTRANCE_POINT.getX() - ZOO_EXIT_POINT.getX()))
                -
                Math.abs(Math.atan2((ZOO_ENTRANCE_LOCATION.getLatitude() - ZOO_EXIT_LOCATION.getLatitude()) * getYLatitudeRatio(),
                        (ZOO_ENTRANCE_LOCATION.getLongitude() - ZOO_EXIT_LOCATION.getLongitude()) * getXLongitudeRatio()));
    }

    public static Point[] getPoints()
    {
        Point[] points = new Point[locations.length];
        for (int i = 0; i < locations.length; i++) {
            points[i] = locationToPoint(locations[i]);
        }
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.getX(),p2.getX()));
        for (Point point :
                points) {
            Log.e("AVIV", point.toString());
        }
        return points;
    }

    public static double getXLongitudeRatio()
    {
        return (ZOO_ENTRANCE_POINT.getX() - ZOO_EXIT_POINT.getX()) / (ZOO_ENTRANCE_LOCATION.getLongitude() - ZOO_EXIT_LOCATION.getLongitude());
    }
    public static double getYLatitudeRatio()
    {
        // NOTE: be aware that the y axis are upside down in contrary to the points
        return (ZOO_EXIT_POINT.getY() - ZOO_ENTRANCE_POINT.getY()) / (ZOO_ENTRANCE_LOCATION.getLatitude() - ZOO_EXIT_LOCATION.getLatitude());
    }
    public static double getSinAlpha() {
        return Math.sin(getAlpha());
    }
    public static double getCosAlpha() {
        return Math.cos(getAlpha());
    }



    public static Point locationToPoint(Location location) {
        Location locationCenteredToEntranceReversed = calibrateLocationToEntranceAndReverseLongitudeAxis(location);
        Location turnedLocation = rotateLocationAroundEntrance(locationCenteredToEntranceReversed);
        Location locationCenteredAndRatioed = scaleLocationToPoints(turnedLocation );

        return new Point((int)locationCenteredAndRatioed.getLongitude() + ZOO_ENTRANCE_POINT.getX(),
                (int)locationCenteredAndRatioed.getLatitude() + ZOO_ENTRANCE_POINT.getY());
    }

    @NonNull
    private static Location calibrateLocationToEntranceAndReverseLongitudeAxis(Location location) {
        // calibrate the position of the location based on the entrance
        Location locationCenteredToEntrance = new Location(location.getLatitude() - ZOO_ENTRANCE_LOCATION.getLatitude(),
                location.getLongitude() - ZOO_ENTRANCE_LOCATION.getLongitude());

        // reverse the latitude axis, so both the before and after would have the same axis
        return new Location(
                -locationCenteredToEntrance.getLatitude(),
                locationCenteredToEntrance.getLongitude()
        );
    }

    @NonNull
    private static Location scaleLocationToPoints(Location locationCenteredToEntranceReversed) {
        // after, it should be centered, with point equaled values
        return new Location(getYLatitudeRatio() * locationCenteredToEntranceReversed.getLatitude(),
                getXLongitudeRatio() * locationCenteredToEntranceReversed.getLongitude());
    }

    @NonNull
    private static Location rotateLocationAroundEntrance(Location locationCenteredAndRatioed) {
        // turn it around
        return new Location(
                locationCenteredAndRatioed.getLatitude() * getCosAlpha()
                        + locationCenteredAndRatioed.getLongitude() * getSinAlpha(),
                locationCenteredAndRatioed.getLongitude() * getCosAlpha()
                        - locationCenteredAndRatioed.getLatitude() * getSinAlpha()
        );
    }

    public Enclosure[] getEnclosures() {
        return enclosures;
    }

    public Animal.PersonalStories[] getAnimalStories() {
        return animalStories;
    }

    public Misc[] getMiscMarkers() {
        return miscMarkers;
    }

    public MapResult getMapResult() {
        return mapResult;
    }

    public WallFeed[] getWallFeeds() {
        return wallFeeds;
    }

    public ContactInfoResult getContactInfoResult() {
        return contactInfoResult;
    }

    public OpeningHoursResult getOpeningHoursResult() {
        return openingHoursResult;
    }

    public Price[] getPrices() {
        return prices;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setEnclosures(Enclosure[] enclosures) {
        this.enclosures = enclosures;
    }

    public void setAnimalStories(Animal.PersonalStories[] animalStories) {
        this.animalStories = animalStories;
    }

    public void setMiscMarkers(Misc[] miscMarkers) {
        this.miscMarkers = miscMarkers;
    }

    public void setMapResult(MapResult mapResult) {
        this.mapResult = mapResult;
    }

    public void setWallFeeds(WallFeed[] wallFeeds) {
        this.wallFeeds = wallFeeds;
    }

    public void setContactInfoResult(ContactInfoResult contactInfoResult) {
        this.contactInfoResult = contactInfoResult;
    }

    public void setOpeningHoursResult(OpeningHoursResult openingHoursResult) {
        this.openingHoursResult = openingHoursResult;
    }

    public void setPrices(Price[] prices) {
        this.prices = prices;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }
}

