package com.tym.shortvideo.filter.base;

/**
 * @author Jliuer
 * @Date 18/04/28 9:27
 * @Email Jliuer@aliyun.com
 * @Description 预览的滤镜
 */
public class GLDisplayFilter extends GLImageFilter {

    public GLDisplayFilter() {
        this(VERTEX_SHADER, FRAGMENT_SHADER_2D);
    }

    public GLDisplayFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
    }
}
