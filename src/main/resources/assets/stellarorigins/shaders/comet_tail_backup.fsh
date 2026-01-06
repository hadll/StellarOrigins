#version 150

in vec2 vUV;
out vec4 fragColor;

uniform sampler2D Texture;
uniform vec3 uPlayerPos;
uniform vec3 uPrevPos;
uniform float uAlpha;
uniform float uWidth;

void main() {
    // sample gradient (white → yellow → transparent)
    vec4 tex = texture(Texture, vUV);

    // simple glow falloff
    float fade = smoothstep(1.0, 0.0, vUV.y);

    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
