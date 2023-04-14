z = 1
read x
read y
if x == 1 then
  write x 
  if y == 2 then
     write y
     z = 3
     if x==1 then
        z=3
     endif
  endif
endif
if z == 3 then
  write z
endif

