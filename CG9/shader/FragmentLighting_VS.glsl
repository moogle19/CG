#version 150 core

in vec3 positionMC;
in vec3 normalMC;
in vec2 texCoords;

uniform mat4 viewProj;
uniform mat4 model;
uniform mat4 modelIT;

out vec3 positionWC;
out vec3 normalWC;
out vec2 fragmentTexCoords;

void main(void)
{
	positionWC = (viewProj * model * vec4(positionMC, 1)).xyz;
	normalWC = normalize(vec3(modelIT * vec4(normalMC, 0)));
	fragmentTexCoords = texCoords;
}