#!/usr/bin/env bash

args=("$@")

java -cp ./ Execution ${args[0]} ${args[1]}
