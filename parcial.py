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
    self.name = 'alu z80'
    
  def suma(self, op1, op2):
    carry = False
    result = bitarray(8)
    for i in range(7,-1,-1):
      result[i] = (op1[i] != op2[i]) != carry
      carry = (op1[i] + op2[i] + carry) / 2 
    return carry,result
    
  def resta(self, op1, op2):
    borrow, addit = self.suma(op1,self.complemento1(op2))
    if borrow:
      carrbit = bitarray('00000001')
      carry, result = self.suma(addit,carrbit)
    else:
      result = self.complemento1(addit)
    return borrow,result
  #def AND(self,eg1,reg2):
  #def OR(self,reg1,reg2):
  #def XOR(self,reg1,reg2):
  #def complemento(self,reg):
  #def desplazamiento(self,reg1,reg2):
  #def registro(self,reg):
  
  def complemento1(self,bits):
    result = bitarray(8)
    for i in range(0,8):
      result[i]= not bits[i]
    return result
  
class Procesor:
  
  def __init__(self, registros, pines, banderas):
    self.pines = pines
    self.registros = registros
    self.banderas = banderas
    self.alu = Alu()

  #to test
  def suma(self, reg1, reg2):
    # ADD A,B
    operator1 = self.registros.get(reg1)
    operator2 = self.registros.get(reg2)
    carry, result = self.alu.suma(operator1,operator2)
    self.banderas['CARRY']=carry
    self.registros.update({'F': result})
  
  def res(self, reg1, reg2):
    # SUB A,B
    operator1 = self.registros.get(reg1)
    operator2 = self.registros.get(reg2)
    carry, result = self.alu.resta(operator1,operator2)
    self.banderas['CARRY']=carry
    self.registros.update({'F': result})
  
    
  def transfer(self,reg1,reg2):
    #LD A,B
    if reg1 in REGISTROS:
      self.registros[reg2]=self.registros.get(reg1)
    #LD B,32H |00110010
    elif len(reg1) == 8 :
      self.registros[reg2] = reg1
    #LD A,(2080H) |10000010000000
    else :
      self.registros[reg2] = MEMORY[int(reg1, 2)]
    
  def inputdatabus(self,indatabus):
    self.pines['databus'] = indatabus

  def inop(self,reg1):
    self.registros[reg1] = self.pines['databus']
    
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

  def printbanderas(self):
    for key in self.banderas:
      print (key," ",self.banderas.get(key))

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

BANDERAS = {
  'SIGN':False,
  'ZERO':False,
  'CARRY':False
}

MEMORY = [bit8]*65536
z80 = Procesor(REGISTROS, PINES, BANDERAS)

test = bitarray('0001010')
print(test)
#Bug first bit has to be 1
print("Input data bus (8 bits)")
databusin = input()
z80.inputdatabus(bitarray(str(databusin)))
z80.inop('A')

print("Input data bus (8 bits)")
databusinp = input()
z80.inputdatabus(bitarray(str(databusinp)))
z80.inop('B')
z80.suma('A','B')
z80.res('A','B')

z80.printpines()
z80.printregisters()
z80.printbanderas()