#version 150 core

uniform mat4 model;
uniform mat4 viewProj;

in vec3 positionMC;
in vec3 vertexColor;

out vec3 fragmentColor;

void main(void) {
    gl_Position = viewProj * model * vec4(positionMC, 1);
	fragmentColor = vertexColor;
}