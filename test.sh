#!/bin/bash
git filter-branch --commit-filter '
        if [ "$GIT_COMMITTER_NAME" = "darcade" ];
        then
                GIT_COMMITTER_NAME="Darcade";
                GIT_AUTHOR_NAME="Darcade";
                GIT_COMMITTER_EMAIL="darcade@darcade.de";
                GIT_AUTHOR_EMAIL="darcade@darcade.de";
                git commit-tree "$@";
        else
                git commit-tree "$@";
        fi' HEAD
