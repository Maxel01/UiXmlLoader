# UiXmlLoader
An UiLoader that loads xml files to LibGdx stages.

The Uiloader can be used to create layouts in xml files instead of hardcoding it in Java-Classes.
Each element provides an onLoad method to manipulate or insert data. Inside the Onload-Method it is possible to access the element via its name and cast it to an Actor.

You can see in the [example.xml](src/de/maxiindiestyle/api/ui/xml/example.xml) file how the xml is constructed. You can also see in the [Example.java](src/de/maxiindiestyle/api/ui/xml/Example.java) how the Onload-Methods and the Varargs can be used.

# Ui Components
There are additional ui components like the Chart or the TabbedTable.
There are also other Classes that extends an Actor, but do not provide additional features. For Example the XmlLabel is only required to provide additional constructors to use the attributes from the xml file. Therefore it is required to use XmlLabel instead of Label. These Classes always begin with 'Xml'.
Ui Components that do not start with 'Xml' provide additionals Components or extends the main class.

# XML Preprocessing
The xml files are preprocessed before loading the file into a stage.
Thus it is possible to:
* include other files
* replace text in other files before loading
* use data from a Java class
* multiply components (especially for including for example Tabs)

For example

  ```xml
  <include src="tab.xml" replace="tabName=[Tab_1, Tab_2, Tab_3]"/>
  ```

turns into

  ```xml
  <include src="tab.xml" replace="tabName=Tab_1"/>
  <include src="tab.xml" replace="tabName=Tab_2"/>
  <include src="tab.xml" replace="tabName=Tab_3"/>
  ```
