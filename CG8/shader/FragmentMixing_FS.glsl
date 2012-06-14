#version 150 core

uniform vec3 inverseLightDirection;

in vec3 normalWC;
in vec4 fragmentColor1;
in vec4 fragementColor2;

out vec4 fragmentColor;

void main(void) {
    fragmentColor = fragmentColor1;
}