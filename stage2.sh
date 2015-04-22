#!/usr/bin/env bash

args=("$@")

java -cp bin/ Execution ${args[0]} ${args[1]}