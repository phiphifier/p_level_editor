To compile, cd to the p_level_editor folder, and run the following commands:
javac main/*
javac ui/*
javac level/*
javac config/*

To run, while still in the p_level_editor folder, run this command:
java main.PLevelEditor

This is my custom level editor! It isn't quite finished yet, but all base functionality is here. If allows for creating 2D tile based levels with layer support. It Includes basic entity placement, which will place instances of EntityDataHolder. Entity data holders contain x/y coordinates, an id, and an additional number for different states. The ability to change the additional state number is not yet implemented. Backgrounds are handled through the background manager, and follow a similar philosophy to entties, allowing for embedding background data including id, x, y, x and y offsets per screen wrap in pixels, and a boolean for whether or not the background is tiled/repeats. Right clicking the layer buttons on the left side of the editor provides additional options, such as toggle visibility, shift layer up/down, insert new layers, and delete functions. The entire level can be cropped to only be as large as it needs to be, or enlarged with a changeable point of origin if you need more space.

Main method is located at main.PLevelEditor

If you compile and run this, it will create a hidden folder in your home directory named ".plvleditor"

For UI, the main classes of interest are probably Button, ButtonListener, Menu, MenuStringInputField, and StringInputFieldListener. Most other menus follow a pretty similar design, defining size, position, buttons, and interacting with other helper classes, mostly from the level package.

For actual level modification, the main classes of interest are MenuEditorTools, MenuLayerSelect, MenuLayerOptions, and anything in the level package.

This is just a hobby project of mine. I'm a self taught java programmer, and this project has been huge in helping me learn more about writing java software. With that said, there's bound to be a lot of crust. lol
