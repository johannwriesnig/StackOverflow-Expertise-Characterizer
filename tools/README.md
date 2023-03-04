***TAKEN FROM RADON REPOSITORY***

Requirements
------------

Radon will run from **Python 2.7** to **Python 3.8** (except Python versions
from 3.0 to 3.3) with a single code base and without the need of tools like
2to3 or six. It can also run on **PyPy** without any problems (currently PyPy
3.5 v7.3.1 is used in tests).

Radon depends on as few packages as possible. Currently only `mando` is
strictly required (for the CLI interface). `colorama` is also listed as a
dependency but if Radon cannot import it, the output simply will not be
colored.

**Note**:
**Python 2.6** was supported until version 1.5.0. Starting from version 2.0, it
is not supported anymore.

Installation
------------

With Pip:

.. code-block:: sh

    $ pip install radon

Or run the setup file in tools/radon-master/:

.. code-block:: sh

    $ python setup.py install