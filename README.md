Small student project for Java, Python, and machine learning practice.
Project implements socket communication between environment written in Java and Python app. 

Java environment is built on Space Runner game from youtube tutorial for JavaFX game development:
 https://www.youtube.com/watch?v=DkIuA5ZEZ_U&list=PL4wcbt63yAbdtY-GOeuRjIePfUsukSJZ9
  
For python app i used examples from Deeplizard tutorial on reinforcement learning:
 https://deeplizard.com/learn/playlist/PLZbbT5o_s2xoWNVdDudn51XM8lOuZ_Njv
 
Socket server is implemented on Java env side while python app acts as a client.
When game is started it waits for python client to start communication and send required commands.
After command is received java app takes required actions and sends back all information required for neural network input which includes rewards and states(images).
 
Software used for development:
 Intellij IDEA,
 Anaconda jupyter notebook


