Main method is located at main.PLevelEditor

If you compile and run this, it will create a hidden folder in your home directory named ".plvleditor"

For UI, the main classes of interest are probably Button, ButtonListener, Menu, MenuStringInputField, and StringInputFieldListener. Most other menus follow a pretty similar design, defining size, position, buttons, and interacting with other helper classes, mostly from the level package.

For actual level modification, the main classes of interest are MenuEditorTools, MenuLayerSelect, MenuLayerOptions, and anything in the level package.

This is just a hobby project of mine. I'm a self taught java programmer, and this project has been huge in helping me learn more about writing java software. With that said, there's bound to be a lot of crust. lol
