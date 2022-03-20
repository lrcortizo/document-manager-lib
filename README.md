### Description
Document manager library. 
This library is designed using a strategy pattern to add more features and support more document types.

Current supported document types:
- PDF

Current features:
- Document size reduction

### How to use
- import the **DocumentManagerService** bean to access the library features through its methods
- Use the **DocumentType** enum class to indicate the wanted document type

### How to add more document types
- Add new processor service class implementation of **DocumentProcessorService** interface
- Create new type in **DocumentType** enum class with its correct processor service class name
- Update this readme info

### How to add more features
- Add new feature to **DocumentProcessorService** interface or create a new interface if it is more appropriate
- Implement the feature in all implementations of the interface or create the new implementation classes for all types in case of a new interface
- Adapt **DocumentManagerService** class to support new feature
- Update this readme info
