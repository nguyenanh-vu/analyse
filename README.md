# analyse

Current version 0.0.1

Tool for analysis on words present in online conversations. Prior to use, user has to download archive files for conversations. Currently working with Whatsapp and Facebook Messenger.

## launch arguments

(Optional) path to scripts to execute can be added as launch arguments. Scripts will be first executed before command line usage.

## use

Command line use with commands:
    echo workdir|address    read working variable
    export                  export session data to JSON file. See also "export help" for additionnal informations
    label                   label management for authors. See also "label help" for additionnal informations
    load                    load data from file. See also "load help" for additionnal informations
    merge                   merge to elements together. See also "merge help" for additionnal informations
    quit                    quit application
    rename                  rename elements. See also "rename help" for additionnal informations
    reset                   reset current session (erase all data)
    run [script name]       run script
    save                    save current working session. If save path has not been defined, use "export session [file path]" instead
    set workdir|address     set working variable 

Possibility to create scripts, with same commands as in command line. 
Use "//" at the beginning to comment line.

## future features

Currently only data preparation features. Search and statistical analysis features to do.