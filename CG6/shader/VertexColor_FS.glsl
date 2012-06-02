#version 150 core

in vec4 fs_in_color;

out vec4 fs_out_color;

void main(void) {
    fs_out_color = fs_in_color;
}