# PyNESTML - The NEST Modelling Language @Python

NESTML is a domain specific language that supports the specification of neuron models
in a precise and concise syntax, based on the syntax of Python. Model equations
can either be given as a simple string of mathematical notation or as an algorithm written
in the built-in procedural language. The equations are analyzed by NESTML to compute
an exact solution if possible or use an appropriate numeric solver otherwise.

## Directory structure

`models` - Example neuron models in NESTML format

`pynestml` - The source code of NESTML

## Installing and running NESTML

In order to execute the language tool-chain, Python in version 2 or 3 is required. A setup file is provided and can be installed by 

```
python2 setup.py install
```

For Python in version 3, respectively:

```
python3 setup.py install
```

Correct installation can be tested by 

```
python3 setup.py test
```

For a correct installation, Python's SetupTools or Distutils are required. In the case, that SetupTools are provided, all remaining requirements are automatically installed, otherwise, the following dependencies have to be installed:

* [SymPy in the version >= 1.0] (http://www.sympy.org/en/index.html)

* [NumPy in the version >=1.8.2] (http://www.numpy.org/)

* [Antlr4 runtime environment in the version >= 4.7] (https://github.com/antlr/antlr4/blob/master/doc/python-target.md)

After the installation, change the current directory to:

```
/pynestml/src/main/python/org/frontend
```

The processing of models can then be executed by the following command:

```
python PyNestMLFrontend.py -ARGUMENTS
```

where arguments are:

| Command | Description |
| -h or --help | Print help message.|
| -path | Path to the source file or directory containing the model.|
| -target | (Optional) Path to target directory where models will be generated to. | 




