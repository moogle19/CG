#version 150 core

uniform mat4 viewProj;
uniform mat4 model;
uniform mat4 modelIT; 

in vec3 modelMC;
in vec3 normalMC;
in vec4 vertexColor;
in vec4 vertexColor2;

out vec3 normalWC;
out vec4 fragmentColor1;
out vec4 fragmentColor2;

void main(void) {
    gl_Position = viewProj * model * vec4(modelMC, 1);
    normalWC = normalize(vec3(modelIT * vec4(normalMC, 1)));
    fragmentColor1 = vertexColor;
    fragmentColor2 = vertexColor2;
}