package xiazdong.me.opengldemo.util;

import static android.opengl.GLES20.*;

/**
 * Created by xiazdong on 17/4/15.
 */

public class ShaderHelper {
    public static final String TAG = ShaderHelper.class.getSimpleName();

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type); //创建一个着色器对象
        if (shaderObjectId == 0) {
            LogUtils.w(TAG, "Could not create new shader.");
            return 0;
        }
        glShaderSource(shaderObjectId, shaderCode);  //关联着色器对象和shader源代码
        glCompileShader(shaderObjectId);  //编译着色器

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0); //

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            LogUtils.w(TAG, "Compile shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            LogUtils.w(TAG, "Could not create program.");
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            LogUtils.w(TAG, "Link program failed.");
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        LogUtils.v(TAG, "Results of validating program: " + validateStatus[0] + "\nLog: " + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
