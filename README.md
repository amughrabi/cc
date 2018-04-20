# Concurrency Control Simulation
A project to simulate the concurrency controls techniques.
# Systems Prerequisites
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8+
* [Maven](https://maven.apache.org/)
* [Git](https://git-scm.com/) - Optional in case of download the project
# Run the project
* Download or clone the project
* cd to pom.xml directory
* Open the terminal run `mvn jetty:run`
* Open the browser and hit `http://localhost:8080`
# Definitions
## Snapshot
It is a picture for the system resources such as disk, buffer, registers, time frame table, logs, lock table, etc at a 
specific period of time
## Disk
It is the data that stored on the physical memory.
## Buffer
It is the data that stored on the temporary memory.
## Registers
It is the data that stored on the CPU memory.
## Time frame Table
It is the table that constructed to order the transactions' operations within a time unit.   
# Conditions
* Each **transaction id** should be unique.
* No transaction defined without operations.
* Each transaction should have `{@id, @accessTimeUnit}` attributes.
* Each operations should have `{@var, @type}` attributes.
* No dashes or spaces allowed in attributes names.
* All variables should be defined in `<disk>...</disk>`
* Allowed mathematical operations are (+, -, /, *) 

# How to write the system XML? 
First you have to define a `<snapshot>` element.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<snapshot>
    ...
</snapshot>
```
The snapshot contains `<disk>` and number of `<transaction>`s 
```xml
<disk>
    <block id="A">10</block>
    <block id="B">9</block>
</disk>
```
The disk contains the default values for the variables that used from transactions, it's possible to be shared between transactions.
```xml
<transaction id="T1" accessTimeUnit="10">
    <operation var="start"   type="start"/>
    <operation var="A"       type="read"/>
    <operation var="A"       type="sub">50</operation>
    <operation var="A"       type="write"/>
    <operation var="failure" type="failure"/>
    <operation var="B"       type="read"/>
    <operation var="B"       type="add">50</operation>
    <operation var="B"       type="write"/>
    <operation var="commit"  type="commit"/>
</transaction>
```
The transaction has a set of operations that used to represent operations; for example, a transaction may contain the following operation:
```text
A = A - 50
B = B + 50
```  
Which this can be converted to
```text
Read(A)
A = A - 50
Write(A)
Read(B)
B = B + 50
Write(B)
```
To be structured as the above xml structure :point_up_2:

**Important**: Make sure every variable in read/write operation has been defined/declared in the disk!
# How To execute?
It's simple, you need to 1) fill the XML, 2) select the recovery method and 3) decide to have strict-2PL... 

If every thing is OK, you need to hit run ... Enjoy! :smiley:
# Licence 
[MIT](https://github.com/amughrabi/cc/blob/master/LICENSE)

Cheers!
Ahmad Al Mughrabi   
