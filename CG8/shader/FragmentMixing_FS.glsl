#version 150 core

uniform vec3 lightDir;

in vec3 normalWC;
in vec4 fragmentColor1;
in vec4 fragmentColor2;

out vec4 fragmentColor;

void main(void) {
    //fragmentColor = fragmentColor1;
    fragmentColor = mix(fragmentColor1, fragmentColor2,  ceil(dot(lightDir, normalWC))); 
}