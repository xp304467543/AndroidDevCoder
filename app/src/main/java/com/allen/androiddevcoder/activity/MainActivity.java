package com.allen.androiddevcoder.activity;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.allen.androiddevcoder.R;
import com.allen.androiddevcoder.util.WeakRefHander;
import com.allen.linechart.LineChart;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import util.MLog;

public class MainActivity extends AppCompatActivity implements WeakRefHander.Callback{
    private List<Animator> mAnimators;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private LineChart lineChart;
    private WeakRefHander weakRefHander;
    private static final int HANDLER_MESSAGE_START = 0;
    private String TAG ="lifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        lineChart = (LineChart) findViewById(R.id.linechart);
        mAnimators = lineChart.createAnimation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent mIntent = new Intent(MainActivity.this, com.allen.androiddevcoder.MainActivity.class);
                startActivity(mIntent);
            }
        });
        lineChart.setAnimationStatus(mAnimators, LineChart.AnimStatus.START);
        weakRefHander = new WeakRefHander(this);
        weakRefHander.start(HANDLER_MESSAGE_START, 1000 * 2);
        MLog.d(TAG, "onCreate: ");
        /**
         * 1）反射构建 Student 对象
         */
        Class studentClass = TestReflect.class;
        try {
            Constructor constructor = studentClass.getConstructor(new Class[]{String.class,int.class});

            TestReflect aClass = (TestReflect) constructor.newInstance("ALlen",17);
            MLog.d(TAG, "onCreate: "+aClass.toString());
        } catch (NoSuchMethodException e) {
            MLog.e(TAG, "onCreate: "+e.getLocalizedMessage() );
        } catch (InstantiationException e) {
            MLog.e(TAG, "onCreate: "+e.getLocalizedMessage() );
        } catch (IllegalAccessException e) {
            MLog.e(TAG, "onCreate: "+e.getLocalizedMessage() );
        } catch (InvocationTargetException e) {
            MLog.e(TAG, "onCreate: "+e.getLocalizedMessage() );
        }
//        /**
//         * 2）反射修改私有变量
//         */
//        try {
//            TestReflect testReflect = new TestReflect("Test",18);
//            MLog.d(TAG, "onCreate:初始值 "+testReflect.getGradle());
//            Field field = TestReflect.class.getDeclaredField("gradle");
//            field.setAccessible(true);
//            field.set(testReflect,250);
//            MLog.d(TAG, "onCreate: "+testReflect.getGradle());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        /**
//         * 3）反射调用私有方法
//         */
//        try {
//            TestReflect reflect = new TestReflect("ALlen",225);
//            Method method  =TestReflect.class.getDeclaredMethod("getSchool",null);
//            method.setAccessible(true);
//            method.invoke(reflect,null);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri!=null){
            String articleId = uri.getQueryParameter("article");
            MLog.d(TAG, "onCreate: "+articleId);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MLog.d(TAG, "onRestart: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        MLog.d(TAG, "onStart: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        MLog.d(TAG, "onStop: ");

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MLog.d(TAG, "onSaveInstanceState: ");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MLog.d(TAG, "onRestoreInstanceState: ");
    }
    /**
     * 找到数值里面的最大值确定绘图区间
     *
     * @param value 指数大师数组
     * @return
     */
    private float getMaxValue(List<Float> value) {
        float temp = 0.0f;
        for (int i = 0; i < value.size(); i++) {
            if (temp < Math.abs(value.get(i) - 3873.43)) {
                temp = Math.abs(value.get(i) - 3873.43f);
            }
        }
        return temp;
    }
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLER_MESSAGE_START) {
            lineChart.setLoading(false);
            final List<Float> lineData = ParseDate(getData);
            lineChart.setNowTime(nowtime);
            /**
             * 昨日收盘大于现在值
             */
            if (true) {
                lineChart.setIsUp(true);
            } else {
                lineChart.setIsUp(false);
            }
            lineChart.setLineData(lineData);
            lineChart.setStartValue(3873.43f);
            lineChart.setNowValue(lineData.get(lineData.size() - 1));
            lineChart.setMaxValue(getMaxValue(lineData));
            lineChart.invalidate();
        }
        return true;
    }

    private List<Float> ParseDate(String data) {
        String[] index = data.split(";");
        List<Float> linedata = new ArrayList<>();
        for (int i = 0; i < index.length; i++) {
            linedata.add(Float.parseFloat(index[i].split(",")[1]));
        }
        nowtime = index[index.length - 1].split(",")[0].toString();
        return linedata;
    }
    String nowtime;
    /**
     * 模拟数据
     */
    String getData = "0930,3873.43,6488345000,11.930,543886100;0931,3874.33,3368922900,12.214,263142000;0932,3873.20,2960691200,12.340,231699200;0933,3871.35,2486288900,12.367,198823900;0934,3875.45,2396155000,12.364,194075900;0935,3879.58,2448239000,12.476,183396600;0936,3879.27,2472663000,12.496,195210300;0937,3876.52,2470259000,12.465,202767200;0938,3873.68,2272074000,12.471,181183500;0939,3870.08,1961041000,12.547,143049900;0940,3866.73,1840553000,12.575,141041700;0941,3864.54,2041130000,12.612,154586500;0942,3863.41,1909899000,12.581,158457500;0943,3865.25,1946988000,12.591,152430400;0944,3868.08,1967558000,12.643,143371200;0945,3868.49,1911872000,12.675,143107600;0946,3868.16,1828412000,12.717,132959100;0947,3870.43,1694958000,12.735,128538900;0948,3872.43,1652068000,12.747,126139100;0949,3874.86,1714885000,12.750,133751200;0950,3878.79,1682670000,12.769,126190200;0951,3879.69,1526995000,12.757,123432800;0952,3880.09,1572000000,12.763,121086900;0953,3876.48,1654360000,12.747,135129700;0954,3873.49,1637796000,12.737,131836000;0955,3873.19,1482875000,12.718,123091200;0956,3873.42,1314297000,12.711,106143200;0957,3873.98,1208534000,12.734,86286000;0958,3872.40,1355968000,12.747,101768000;0959,3869.05,1468588000,12.756,111785000;1000,3864.11,1682832000,12.759,130755200;1001,3860.58,1343079000,12.767,101915500;1002,3859.41,1383675000,12.780,102998800;1003,3861.26,1245416000,12.801,88790400;1004,3863.26,1383565000,12.818,100860300;1005,3865.55,1245017000,12.840,87488200;1006,3866.03,1284885000,12.863,90158700;1007,3866.25,1112821000,12.879,79522900;1008,3865.84,1234695000,12.910,81869400;1009,3864.39,1094960000,12.918,80953900;1010,3863.52,1110674000,12.935,78256200;1011,3863.23,964950000,12.933,75316900;1012,3863.73,1089310000,12.934,84179300;1013,3864.70,1006463000,12.942,73575300;1014,3865.60,925861000,12.949,68214700;1015,3864.72,1255754000,12.967,88518500;1016,3862.80,1242367000,12.979,89469500;1017,3860.09,1360194000,12.984,102449100;1018,3858.07,1278887000,12.985,98143500;1019,3856.17,1275016000,12.972,104853100;1020,3853.70,1276601000,12.964,102857300;1021,3854.75,1262667000,12.963,97494900;1022,3856.43,1098659000,12.967,82649400;1023,3858.67,1059837000,12.974,78083700;1024,3859.59,871714000,12.985,60907200;1025,3859.75,787094000,12.996,54850900;1026,3859.63,826397000,13.002,60110900;1027,3858.08,815272000,13.005,60694500;1028,3858.19,1011009000,13.014,72905500;1029,3861.24,1089225000,13.033,72751600;1030,3864.09,1102317000,13.047,76282600;1031,3866.47,1265752000,13.058,90987500;1032,3869.07,1353233000,13.071,95797600;1033,3870.05,1245605000,13.080,90084300;1034,3870.02,1090772000,13.083,81513400;1035,3869.97,1151635000,13.085,86491800;1036,3868.65,1057778000,13.096,73913800;1037,3867.28,842686000,13.106,58285700;1038,3866.82,706996000,13.110,51460800;1039,3866.66,704910000,13.118,48799100;1040,3866.02,693637000,13.122,50422500;1041,3863.84,702183000,13.127,50422400;1042,3862.08,811702000,13.132,58737200;1043,3856.36,1165086000,13.126,92143200;1044,3854.12,1076478000,13.124,83523600;1045,3854.38,1040515000,13.127,77213900;1046,3855.84,1147609000,13.121,91825200;1047,3856.90,887382000,13.123,65743500;1048,3857.30,677269000,13.125,50899100;1049,3857.60,722260000,13.121,57453400;1050,3858.58,851237000,13.130,58981900;1051,3858.45,789284000,13.131,58909400;1052,3858.71,854901000,13.135,62898700;1053,3861.62,1151602000,13.142,82662000;1054,3863.36,1166114000,13.153,80900100;1055,3863.98,961731000,13.165,64445500;1056,3863.45,837921000,13.175,56553800;1057,3861.80,799683000,13.182,55685800;1058,3860.91,833346000,13.188,59083500;1059,3860.61,655588000,13.194,45176200;1100,3860.00,732254000,13.200,51310400;1101,3860.78,754822000,13.209,50510300;1102,3861.23,750106000,13.215,52077300;1103,3859.83,828550000,13.218,60959400;1104,3856.80,913712000,13.217,69916300;1105,3851.62,1143933000,13.211,91009700;1106,3848.68,1428267000,13.191,123107300;1107,3845.91,1349864000,13.180,110482100;1108,3846.68,1156169000,13.175,92108600;1109,3848.56,865980000,13.180,61498000;1110,3850.20,670150000,13.179,51800700;1111,3849.84,589820000,13.177,45968000;1112,3848.84,662660000,13.174,52673400;1113,3847.23,700560000,13.178,50717700;1114,3846.24,684730000,13.176,52886200;1115,3844.52,790020000,13.175,61321300;1116,3842.68,872820000,13.170,70062000;1117,3843.08,884180000,13.165,71383700;1118,3842.15,793530000,13.164,61304800;1119,3844.99,680680000,13.160,55004300;1120,3846.54,647100000,13.159,49411500;1121,3848.80,539740000,13.163,38176700;1122,3849.33,553760000,13.164,40724300;1123,3849.99,557900000,13.168,39151600;1124,3849.44,511280000,13.167,39487100;1125,3848.73,487670000,13.168,36298000;1126,3848.86,463770000,13.171,32667800;1127,3848.12,470830000,13.172,35225900;1128,3848.23,407550000,13.175,28486400;1129,3848.71,415850000,13.179,28058600;1130,3848.61,75250000,13.180,4793000;1300,3849.72,962720000,13.177,75478500;1301,3850.39,387010000,13.181,26297700;1302,3850.93,383240000,13.180,29210300;1303,3849.96,401550000,13.180,30932200;1304,3850.12,433770000,13.184,29631600;1305,3849.68,377640000,13.186,26253700;1306,3850.06,386070000,13.189,26891100;1307,3850.30,451820000,13.193,30743200;1308,3851.25,471380000,13.197,32044800;1309,3852.22,410270000,13.200,29160400;1310,3854.24,437750000,13.202,31013600;1311,3855.92,523020000,13.206,36110800;1312,3857.23,545010000,13.207,40264600;1313,3857.15,488920000,13.212,32922400;1314,3856.96,507480000,13.215,35498000;1315,3856.47,406150000,13.218,28239000;1316,3856.16,453720000,13.221,31539600;1317,3855.93,463450000,13.223,33632600;1318,3856.36,513870000,13.222,39172700;1319,3856.22,514480000,13.228,33738100;1320,3856.50,493060000,13.233,32850300;1321,3855.45,503930000,13.238,33089200;1322,3854.44,551510000,13.243,37474500;1323,3852.93,544390000,13.247,37622100;1324,3851.94,655110000,13.256,41523500;1325,3852.90,744520000,13.268,45039600;1326,3854.77,624260000,13.277,38631200;1327,3855.85,628220000,13.286,38537000;1328,3856.67,574960000,13.293,37120400;1329,3858.32,661000000,13.295,48361900;1330,3859.14,583290000,13.292,46248700;1331,3860.42,669040000,13.297,45802700;1332,3861.57,637450000,13.298,46481100;1333,3861.38,670520000,13.306,43695200;1334,3861.98,655310000,13.313,42713200;1335,3862.07,846740000,13.322,54587900;1336,3862.65,720650000,13.328,48032200;1337,3863.46,721350000,13.340,43463200;1338,3863.05,775860000,13.352,46633000;1339,3861.26,775280000,13.356,53940800;1340,3859.58,799310000,13.360,56227900;1341,3858.83,704010000,13.367,46047700;1342,3858.90,755120000,13.372,50924400;1343,3859.46,750900000,13.374,54637200;1344,3860.41,670800000,13.377,47156700;1345,3860.88,662510000,13.383,43752500;1346,3860.66,721880000,13.387,49978200;1347,3859.25,686630000,13.393,45088000;1348,3857.12,710180000,13.397,49067000;1349,3855.03,759410000,13.401,52840700;1350,3853.63,819400000,13.397,65372200;1351,3853.58,672480000,13.397,50211100;1352,3854.28,600310000,13.400,41794800;1353,3855.69,612020000,13.405,40561000;1354,3858.04,644080000,13.408,45444000;1355,3859.95,750800000,13.415,48225000;1356,3861.48,674600000,13.419,46633000;1357,3863.07,746020000,13.421,53705000;1358,3865.22,770720000,13.427,50894000;1359,3865.88,851790000,13.435,55838000;1400,3867.22,796910000,13.436,58327000;1401,3868.76,1037500000,13.443,69591000;1402,3870.00,1014060000,13.449,69318000;1403,3871.44,933880000,13.449,69341000;1404,3872.53,809630000,13.453,56388000;1405,3871.51,787520000,13.449,62417000;1406,3870.83,910670000,13.450,66867000;1407,3870.32,766360000,13.445,61439000;1408,3870.45,664650000,13.444,50996000;1409,3871.60,769350000,13.446,55568000;1410,3873.86,896680000,13.448,64609000;1411,3876.15,1035320000,13.453,71098000;1412,3877.71,1059290000,13.460,70730000;1413,3878.02,908220000,13.460,67825000;1414,3876.32,839970000,13.462,60081000;1415,3872.94,759820000,13.463,55655000;1416,3870.67,761880000,13.465,54762000;1417,3869.94,666550000,13.467,46246000;1418,3870.64,627220000,13.468,45695000;1419,3869.99,722060000,13.467,55300000;1420,3869.11,746810000,13.470,51855000;1421,3867.72,783960000,13.472,55559000;1422,3864.88,912630000,13.461,80609000;1423,3863.61,791200000,13.463,56277000;1424,3863.88,678600000,13.467,45980000;1425,3865.97,738240000,13.469,52553000;1426,3867.73,714500000,13.471,51230000;1427,3867.76,772030000,13.471,56750000;1428,3867.86,703000000,13.474,49333000;1429,3866.26,816470000,13.478,55373000;1430,3866.00,779720000,13.479,56601000;1431,3867.01,845780000,13.479,62553000;1432,3868.81,932170000,13.459,92562000;1433,3870.55,1219310000,13.421,135272000;1434,3872.67,1062920000,13.406,97263000;1435,3874.43,984530000,13.403,76818000;1436,3874.99,1040690000,13.404,77230000;1437,3875.22,1044370000,13.406,75147000;1438,3875.59,873900000,13.408,62349000;1439,3875.24,858170000,13.412,59777000;1440,3874.60,908860000,13.413,66272000;1441,3873.59,1049250000,13.415,75757000;1442,3872.90,1055560000,13.424,67385000;1443,3873.12,1038450000,13.435,64063000;1444,3873.11,985540000,13.444,62222000;1445,3872.76,1003240000,13.450,66805000;1446,3873.00,994920000,13.458,64664000;1447,3873.27,1094870000,13.466,71039000;1448,3874.35,1093360000,13.472,73355000;1449,3874.91,1207440000,13.483,76591000;1450,3875.62,1329320000,13.495,83156000;1451,3875.91,1209540000,13.502,80332000;1452,3876.20,1289000000,13.510,85857000;1453,3876.12,1390080000,13.511,100375000;1454,3875.49,1407220000,13.517,97192000;1455,3875.29,1367420000,13.520,96804000;1456,3875.46,1687890000,13.520,124584000;1457,3875.30,1072470000,13.518,82502000;1458,3875.84,1011210000,13.516,77471000;1459,3876.64,1173260000,13.512,92179000;1500,3876.73,1356480000,13.522,87554000";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   
}
