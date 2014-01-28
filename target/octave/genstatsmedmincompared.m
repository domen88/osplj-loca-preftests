function S = genstatsmedmincompared(B, prefixa, prefixb, postfix,  X0 = 20000, X1 = 100000, titlep, xlabelp, ylabelp, legenda, legendb, scale = 2000)
  S = [];
  T = [];

  for i = 1:size(B)(2)
    fn = strcat(prefixa, int2str(B(i)));
    fn = strcat(fn, postfix);
    D = load (fn);
    D = D(X0:1:X1)/scale;
    
    fn = strcat(prefixb, int2str(B(i)));
    fn = strcat(fn, postfix);
    E = load (fn);
    E = E(X0:1:X1)/scale;
    
    #       1         2           3               4               5             6       7       8    
    M = [median(D)  min(D) prctile(D, 99) prctile(D, 99.9) prctile(D, 99.999) std(D)  std(D) iqr(D) ]
    N = [median(E)  min(E) prctile(E, 99) prctile(E, 99.9) prctile(E, 99.999) std(E)  std(E) iqr(E) ]
    S = [S; M];
    T = [T; N];
  endfor

  # Min/Med
  figure();

  plot(B, S(:,1), "b-s");
  hold on;
  plot(B, T(:,1), "r-s");
  title(titlep);
  legend(legenda, legendb);
  xlabel(xlabelp);
  ylabel(ylabelp);
  grid();

  
endfunction
