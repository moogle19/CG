#version 150 core

in vec3 fragmentColor;

out vec4 finalColor;

void main(void) {
    finalColor = vec4(fragmentColor, 1);
}