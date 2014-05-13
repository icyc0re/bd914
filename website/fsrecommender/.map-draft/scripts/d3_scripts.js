    

    //GENERATE (MOCK) GENDER RATIO
    var per = Math.random();
    var data = [{'label': 'women', 'value':+per},{'label': 'men', 'value':+(1-per)}];

    
    var r = 15;

    var colorScale = d3.scale.ordinal()
                    .domain(["women", "men"])
                    .range(["#F99", "#99F"]);

    //Find location of where we want to put the statistic and add it                
    var vis = d3.select("#rec" + i + "_stat")
        .append("svg:svg")        
        .data([data])
            .attr("width", r*2) 
            .attr("height", r*2)
        .append("svg:g") 
            .attr("transform", "translate(" + r + "," + r + ")")
 

    //Create pie chart
    var arc = d3.svg.arc()
        .outerRadius(r);
 
    var pie = d3.layout.pie()
        .value(function(d) { return d.value; });
    var arcs = vis.selectAll("g.slice")
        .data(pie)
        .enter() 
            .append("svg:g")
                .attr("class", "slice");
        arcs.append("svg:path")
                .attr("fill", function(d) { return colorScale(d['data']['label']); } ) 
                .attr("d", arc); 
 
        // arcs.append("svg:text")
        //         .attr("transform", function(d) { 
        //         d.innerRadius = 0;
        //         d.outerRadius = r;
        //         return "translate(" + arc.centroid(d) + ")";
        //     })
        //     .attr("text-anchor", "middle")
        //     .text(function(d, i) { return data[i].label; }); 