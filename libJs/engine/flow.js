var Flow=function(){var d,a=[],b=setTimeout(function(){b=null;d._next()},0);return d={destroy:function(){b&&clearTimeout(b)},par:function(j,b){(b||!(a[a.length-1]instanceof Array))&&a.push([]);a[a.length-1].push(j);return d},seq:function(a){return d.par(a,!0)},_next:function(b,k){for(var e=[],f=[],g=a.shift()||[],h=g.length,i=1==h,c=0;c<g.length;c++)(function(a,c){a(function(a,b){e[c]=a;f[c]=b;0==--h&&d._next(i?e[0]:e,i?f[0]:f)},b,k)})(g[c],c)}}};
"undefined"!==typeof module&&"exports"in module&&(module.exports=Flow);"function"===typeof define&&define.amd&&define("Flow",[],function(){return Flow});