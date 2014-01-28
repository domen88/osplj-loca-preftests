function S = genstatsmedmin99(B, prefix, postfix,  X0 = 20000, X1 = 100000, titlep, xlabelp, ylabelp, scale = 2000)
  S = [];

  for i = 1:size(B)(2)
    fn = strcat(prefix, int2str(B(i)));
    fn = strcat(fn, postfix);
    D = load (fn);
    D = D(X0:1:X1)/scale;
    
    #       1         2           3               4               5             6       7       8    
    M = [median(D)  min(D) prctile(D, 99) prctile(D, 99.9) prctile(D, 99.999) std(D)  std(D) iqr(D) ]
    S = [S; M];
  endfor

  # Min/Med/99.9
  figure();

  plot(B, S(:,1), "b-s");
  hold on;
  plot(B, S(:,2), "g-s");
  plot(B, S(:,4), "r-s");
  title(titlep);
  legend("median", "min", "99.9%");
  xlabel(xlabelp);
  ylabel(ylabelp);
  grid();

  
endfunction
