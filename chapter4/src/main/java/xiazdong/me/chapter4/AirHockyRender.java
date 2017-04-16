package xiazdong.me.chapter4;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import xiazdong.me.util.AppConfig;
import xiazdong.me.util.ShaderHelper;
import xiazdong.me.util.TextResourceReader;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;


/**
 * Created by xiazdong on 17/4/5.
 */

public class AirHockyRender implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;

    public AirHockyRender(Context context) {
        //顶点属性数组
        float[] tableVertices = {
                //桌子边框
                -0.55f, 0.53f,
                0.55f, 0.53f,
                -0.55f, -0.53f,

                0.55f, 0.53f,
                -0.55f, -0.53f,
                0.55f, -0.53f,
                //桌子
                -0.5f, -0.5f,
                0.5f, -0.5f,
                -0.5f, 0.5f,

                0.5f, -0.5f,
                -0.5f, 0.5f,
                0.5f, 0.5f,
                //中间线
                -0.5f, 0f,
                0.5f, 0f,
                //木椎
                0f, 0.25f,
                0f, -0.25f
        };
        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVertices);
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f,0f,0f,0f);
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (AppConfig.ENABLE_LOG) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);

        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        vertexData.position(0);
        /**
         * 1. 属性的位置
         * 2. 一个属性有多少个数据，这里是(x,y)，因此是2
         * 3. 数据类型
         */
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //桌子边框
        glUniform4f(uColorLocation, 1f, 0f, 1f, 1f);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        //桌子
        glUniform4f(uColorLocation, 1f, 1f, 1f, 1f); //设置shader中u_Color的值
        glDrawArrays(GL_TRIANGLES, 6, 6); //第三个参数：读入6个顶点
        //中间线
        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        glDrawArrays(GL_LINES, 12, 2);
        //木椎
        glUniform4f(uColorLocation, 0f, 0f, 1f, 1f);
        glDrawArrays(GL_POINTS, 14, 1);
        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        glDrawArrays(GL_POINTS, 15, 1);
    }
}
