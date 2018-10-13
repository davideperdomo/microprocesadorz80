"""
PINES
  PINES SALIDA (LEDs)
  -System Control
    MREQ :19
    IORQ :20
    RD :21
    WR :22
    RFSH :28
    
  -CPU control    
    HALT :18
    
  -Address Bus
    A0-A15 (16 bits)
  
  PINES ENTRADA
  -CPU control
    INT :16
    WAIT :24
    RESET :25
    
    BUSRQ :25
    
  PINES HIBRIDOS
  -Data bus
    D0-D7 (8 bits)

REGISTROS
-Grupo 1
  A ~> Acumulador
  B,C,
  D,E,
  H,L

-Grupo 2 ;stack
  A'
  B',C',
  D',E',
  H',L'

-Contador (16 bits)
  PC 

- Apuntador Stack (16 bits)
  SP 

-Indice (16 bits)
  IX
  IY

-Interrupciones
  I
  
BANDERAS DE ESTADO
  SIGN(S)
  ZERO(Z)
  CARRY(C)
  
ALU
 suma binaria (sumar - restar)
 operaciones logicas ( AND - OR - OREX)
 complemento a 2
 desplazamiento 1 bit
 registro resultados
 comparar
 acceso a bit ( cambiar a 1 - cambiar a 0 - comprobar)
"""
from __future__ import print_function
from bitarray import bitarray

class Alu:
  
  def __init__(self):
    self.alu = 'alu z80'
    
  #def suma(self, reg1, reg2):
  #def resta(self, reg1, reg2):
  #def AND(self,eg1,reg2):
  #def OR(self,reg1,reg2):
  #def XOR(self,reg1,reg2):
  #def complemento(self,reg):
  #def desplazamiento(self,reg1,reg2):
  #def registro(self,reg):
    
class Procesor:
  
  def __init__(self, registros, pines):
    self.pines = pines
    self.registros = registros
    self.alu = Alu()

  def swap(self,reg1,reg2):
    REGISTROS[reg2]=REGISTROS.get(reg1)
  
  def inputdatabus(self,indatabus):
    self.pines['databus'] = indatabus

  def printregisters(self):
    for key in self.registros:
      print (key,end=': ')
      for pin in self.registros.get(key):
        if pin:
          print ('1', end='')
        else:
          print ('0', end='')
      print (" ")
      
  def printpines(self):
    for key in self.pines:
      print (key,end=': ')
      for pin in self.pines.get(key):
        if pin:
          print ('1', end='')
        else:
          print ('0', end='')
      print (" ")

bit8 = bitarray(8)
bit16 = bitarray(16)
bit8.setall(False)
bit16.setall(False)

PINES = {
  'MREQ': [False],
  'IORQ': [False],
  'RD': [False],
  'WR': [False],
  'RFSH': [False],
  'INT': [False],
  'WAIT': [False],
  'RESET': [False],
  'BUSRQ': [False],
  'addresbus' : bit16,
  'databus': bit8
}

REGISTROS = {
  'A':bit8,
  'B':bit8,
  'C':bit8,
  'D':bit8,
  'E':bit8,
  'F':bit8,
  'PC':bit16,
  'SP':bit16,
  'IX':bit16,
  'IY':bit16
}


z80 = Procesor(REGISTROS, PINES)
print("Input data bus (8 bits)")
databusin = input()
z80.inputdatabus(bitarray(str(databusin)))
z80.printpines()
z80.printregisters()