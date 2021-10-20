public class Main {
    public static void main(String args[]) {

        float dist1[] = new float[4];
        test(dist1);
    }
    public static void test(float dist1[]) {
        try{

            float point[]=new float[3];
            Location loc = new Location();

            //获得坐标
            point[0] = (float) 0;
            point[1] = (float) 0;
            point[2] =(float) 1300;
            loc.set_point(point,0);

            point[0] = (float) 5000;
            point[1] = (float) 0;
            point[2] = (float) 1700;
            loc.set_point(point,1);

            point[0] = (float) 0;
            point[1] = (float) 5000;
            point[2] = (float) 1700;
            loc.set_point(point,2);

            point[0] = (float) 5000;
            point[1] = (float) 5000;
            point[2] = (float) 1300;
            loc.set_point(point,3);

            //distance
            loc.set_distance(dist1[0],1);
            loc.set_distance(dist1[1],2);
            loc.set_distance(dist1[2],3);
            loc.set_distance(dist1[3],4);

            //calc
            float x[] = loc.calc();
            if (x == null)
            {
                System.out.println("fail");
            }
            else
            {
                System.out.println(x[0]+","+x[1]+","+ x[2]);
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
class Location {
    //空间已知4点坐标
    float p[][] = new float[4][3];
    //空间已知4点距离
    float d[] = new float[4] ;

    //初始化空间4点坐标
    //p:坐标,数组
    //num:1-4
    void set_point(float point[],int num)  throws Exception
    {
        int j = 0;

        for (j = 0;j < 3;j++)
        {
            p[num - 1][j] = point[j];
        }
    }

    //初始化空间4点距离
    //distance:距离
    //num:1-4
    void set_distance(float distance,int num)  throws Exception
    {
        d[num - 1] = distance;
    }

    //计算未知点坐标
    //p:计算后的返回值
    //fail:back -1
    float[] calc()  throws Exception
    {
        float point[]=new float[3];
        //矩阵A
        float A[][] = new float[3][3];
        //矩阵B
        float B[]= new float[3];
        int i = 0;
        int j = 0;

        //初始化B矩阵
        for (i = 0;i < 3;i++)
        {
            B[i] = (LocationMath.d_p_square(p[i + 1]) - LocationMath.d_p_square(p[i]) - (d[i + 1] * d[i + 1] - d[i] * d[i])) / 2;
        }

        //初始化A矩阵
        for (i = 0;i < 3;i++)
        {
            for (j = 0;j < 3;j++)
            {
                A[i][j] = p[i + 1][j] - p[i][j];
            }
        }

        //计算未知点坐标
        point = LocationMath.solve(A,B);

        return point;
    }
}
 class LocationMath {
    public static void printf_matrix(float m[][]) throws Exception{
        int i = 0;
        int j = 0;

        for (i = 0;i < 3;i++)
        {
            for (j = 0;j < 3;j++)
            {
                System.out.println(m[i*3][j]);
            }
        }

    }

    //三维行列式的值
    //m:3 * 3数组
    public static double det(float m[][]) throws Exception{
        double value = 0.0;

        value = m[0][0] * m[1][1] * m[2][2] +
                m[0][1] * m[1][2] * m[2][0] +
                m[0][2] * m[1][0] * m[2][1] -
                m[0][1] * m[1][0] * m[2][2] -
                m[0][2] * m[1][1] * m[2][0] -
                m[0][0] * m[1][2] * m[2][1];

        return value;
    }

    //将一个行列式的值赋给另一个
    //src,dst:3 * 3数组
    public static void copy_matrix(float src[][],float dst[][]) throws Exception {
        int i = 0;
        int j = 0;

        for (i = 0;i < 3;i++)
        {
            for (j = 0;j < 3;j++)
            {
                dst[i][j] = src[i][j];
            }
        }

    }

    //解方程
    //m:方阵,3 * 3数组
    //b:解
    //x:返回值
    //fail:back -1
    public static float[] solve(float m[][],float b[]) throws Exception {

        float det_m;
        float det_m_temp;
        float m_temp[][] = new float[3][3];
        int i = 0;
        int j = 0;

        float x[]=new float[3];

        det_m = (float) det(m);
        if (det_m == 0)
        {
            return null;
        }
        for (j = 0;j < 3;j++)
        {
            //得到新的行列式
            copy_matrix(m,m_temp);
            for (i = 0;i < 3;i++)
            {
                m_temp[i][j] = b [i];
            }
            det_m_temp = (float) det(m_temp);

            //求解
            x[j] = det_m_temp / det_m;
        }

        return x;

    }

    //计算空间点到原点距离的平方
    public static float d_p_square(float p[]) throws Exception {

        float d = 0;
        int i = 0;

        for (i = 0;i < 3;i++)
        {
            d += p[i] * p [i];
        }

        return d;

    }
}
