
function addSegments(json){
    
    //COLORS
    var MAXSTROKEWEIGHT = 20;
    
    var listColors = new Array();
    createColors();

    
    //SKETCH SIZE
    
    var SKETCH_WIDTHf = 800;
    var SKETCH_HEIGTHf = 800;
    var SKETCH_CENTER_X = SKETCH_WIDTHf / 2;
    var SKETCH_CENTER_Y = SKETCH_HEIGTHf / 2;
    
    
    //SPACE BETWEEN SEGMENTS
    var INTERSTICE_SEGMENT = 0.2;
    
    //SPACE BETWEEN LABELS AND CIRCLE
    var INTERSTICE_LABEL = 7;
    
    // WIDTH OF LABEL (HAS TO BE DETERMINED IN ADVANCE...)
    var widthLabel = 300;
    
    //SPACE BETWEEN BEZIER CURVES AND CIRCLES
    var INTERSTICE_BEZIER = 7;
    
    //PARAMETER HIGHLIGHT
    var opacityDecrease = 0.4;
    
    //INTERACTION
    var currClickedAuthor;
    
    
    //LABEL FOR MAIN SEGMENT
    var mainSegmentLabel;
    
    function  Segment (newLabel, newCount, newIsMain){
        this.label = newLabel;
        this.count = newCount;
        this.isMain = newIsMain;
        if (newIsMain == true){
            mainSegmentLabel = newLabel;
        }
        function toString(){
            return (this.label);
        }
    }
    
    
    var segments = new Array();
    //    segments.push(new Segment("test coauthor 1",5,false));
    //    segments.push(new Segment("test coauthor 2",3,false));
    //    segments.push(new Segment("test coauthor 3",8,false));
    //    segments.push(new Segment("test coauthor 4",2,false));
    //    segments.push(new Segment("test coauthor 5",1,false));
    //    segments.push(new Segment("coco",1,false));
    //    segments.push(new Segment("test main author",1,true));
    
    var data = $.parseJSON(json);
    if(data) {
        for(i=0; i<data.length; i++) {
            var segment = data[i];
            //                        alertHelloWorld();
            //                    alert("hello world!");
            //                    addSegment(segment.label, segment.count,segment.isMain);
            segments.push(new Segment(segment.label,segment.count,segment.isMain));
        }
    }
    
    
    function resetSegments(){
        segments = new Array();
    }
    function alertHelloWorld(){
        alert("hello world!");
    }

    function addSegment(label,count,isMain){
        alert ('made it!');
        segments.push(new Segment(label,count,isMain));
    }
    
    function resetSegments(){
        segments = new Array();
    }


    
    //NUMBER SEGMENTS
    var nbSegments = segments.length - 1;
    console.log("nb Segments without main one: "+nbSegments);
    
    
    //RESIZE SKETCH ACCORDINGLY
    SKETCH_WIDTHf = Math.min(800 + nbSegments*7,1200);
    SKETCH_HEIGTHf = SKETCH_WIDTHf*0.75;
    SKETCH_CENTER_X = SKETCH_WIDTHf / 2;
    SKETCH_CENTER_Y = SKETCH_HEIGTHf / 2;
    

    //REDRAW THE SKETCH AT THESE NEW DIMENSIONS
    var stage = new Kinetic.Stage({
        container: document.getElementById('circleCoAuthors'),
        width: SKETCH_WIDTHf,
        height: SKETCH_HEIGTHf
    });
    console.log("kinetic stage created");

    

    var layerArcBandMain = new Kinetic.Layer();
    var layerArcBandsRegular = new Kinetic.Layer();
    var layerLabelMain = new Kinetic.Layer();
    var labelMain;
    var layerLabelSegments = new Kinetic.Layer();
    
    console.log("kinetic layers successfully instantiated");
    
    //TEXT SIZE
    var textSize = 10+ (SKETCH_WIDTHf-600)/100 - nbSegments/20;
    
    
    //CIRCLE SIZE
    
    var OUTER_CIRCLE_DIAMETER = SKETCH_WIDTHf * 0.5;
    var INNER_CIRCLE_DIAMETER = OUTER_CIRCLE_DIAMETER*9/10;
    var OUTER_CIRCLE_RADIUS = OUTER_CIRCLE_DIAMETER / 2;
    var INNER_CIRCLE_RADIUS = INNER_CIRCLE_DIAMETER / 2;
    
    var TWO_PI = Math.PI*2;
    var PI = Math.PI;
    
    
    
    var nbBasicUnitsSegments = 0;
    for (i = 0;i<segments.length;i++){
        if (segments[i].isMain==true)continue;
        nbBasicUnitsSegments = nbBasicUnitsSegments+segments[i].count;
    }
    
    //the segment for the author should be between 1/8 and 1/3 of a circle. Between that, it varies with the number of partners.
    var radiusUnitAuthor = Math.max(2 / 16, Math.min(1 / nbSegments, 1 / 3));
    //       println("radius Main is:" + radiusUnitAuthor);
    
    //WARNING this unit does not represent the size of a segment for one partner, but the atomic unit for such a size.
    // => a partner with 5 relations with the author will have a segment size of 5 x this atomic unit
    
    var radiusUnitSegment;
    radiusUnitSegment = (1 - INTERSTICE_SEGMENT) * ((1 - radiusUnitAuthor) / (nbBasicUnitsSegments));
    //      println("radius Segment is: "+ radiusUnitSegment);
    
    var interstice = (1 - (radiusUnitSegment * nbBasicUnitsSegments + radiusUnitAuthor)) / (nbSegments + radiusUnitAuthor + 1);
    
    console.log("all preliminary parameters set");
    
    // DRAWING MAIN SEGMENT ---------------------
    var startArcMain = TWO_PI *  (0.5 - radiusUnitAuthor / 2);
    var endArcMain = TWO_PI *  (0.5 + radiusUnitAuthor / 2);
    

    var arcBandMain = new Kinetic.Shape({
        drawFunc: function(context) {
            context.beginPath();
            context.arc(SKETCH_CENTER_X, SKETCH_CENTER_Y, OUTER_CIRCLE_RADIUS, startArcMain, endArcMain, false);
            //            context.lineTo(xLine1,yLine1);
            context.arc(SKETCH_CENTER_X, SKETCH_CENTER_Y, INNER_CIRCLE_RADIUS, endArcMain, startArcMain, true);

            //context.quadraticCurveTo(SKETCH_CENTER_X-(INNER_CIRCLE_RADIUS+OUTER_CIRCLE_RADIUS)/2,SKETCH_CENTER_Y,xLine2,yLine2);
            context.closePath();
            this.fill(context);
        //            this.stroke(context);
        },
        fill: 'red',
        stroke: 'black',
        strokeWidth: 4
    });
    layerArcBandMain.add(arcBandMain);
    stage.add(layerArcBandMain);
    
    console.log("segment for main author drawn");
    
    //ADDS LABEL FOR MAIN SEGMENT
    labelMain = new Kinetic.Text({
        x: SKETCH_CENTER_X-OUTER_CIRCLE_RADIUS-5 - widthLabel,
        y: SKETCH_CENTER_Y,
        text: mainSegmentLabel,
        align:'right',
        width:widthLabel,
        fontSize: 15,
        fontFamily: 'Calibri',
        textFill: 'red'
    });
    labelMain.x = labelMain.x - labelMain.getSize();
    layerLabelMain.add(labelMain);
    stage.add(layerLabelMain);
    console.log("label for main author drawn");
    

    // DRAWING ALL OTHER SEGMENTS
    var countSegments = 0;
    var countBasicUnits = 0;
    var segmentsArr = [];
    
    
    for (i = 0; i < nbSegments; i++) {
        
        var currCountBasicUnits = segments[i].count;
        funcDrawSegment(i);
        countSegments = countSegments + 1;
        countBasicUnits = countBasicUnits + currCountBasicUnits;
    //            println("accumulated count of total basic units treated so far: " + countBasicUnits);
    }
    stage.add(layerArcBandsRegular);
    console.log("all segments for other authors drawn");


    countSegments = 0;
    countBasicUnits = 0;
    

    // DRAWING LABELS FOR OTHER SEGMENTS    
    var labelsArr = [];
    var bezierArr = [];
    
    // START POINT FOR THE BEZIER CURVES (THAT IS, CLOSE TO THE MAIN SEGMENT)
    var x1 = SKETCH_CENTER_X + Math.cos(PI) * (INNER_CIRCLE_RADIUS - INTERSTICE_BEZIER);
    var y1 = SKETCH_CENTER_Y + Math.sin(PI) * (INNER_CIRCLE_RADIUS - INTERSTICE_BEZIER);
  


    for (i = 0; i < nbSegments; i++) {
        
        if (segments[i].isMain == true) continue;
    
        var theta = TWO_PI * (0.5 + radiusUnitAuthor / 2 + interstice * (countSegments + 1) + radiusUnitSegment * countBasicUnits + radiusUnitSegment * segments[i].count / 2);
        funcDrawBezier(i);
        funcDrawLabel(i);

        countSegments = countSegments + 1;
        countBasicUnits = countBasicUnits + segments[i].count;

    }
    stage.add(layerLabelSegments);
    console.log("all labels for other authors drawn");



    function funcDrawSegment(i) {
        //        console.log("label of the curr segment: "+segments[i].label);

        segmentColor = listColors[i];
          

        var startArcRadius = TWO_PI * (0.5 + radiusUnitAuthor / 2 + interstice * (countSegments + 1) + radiusUnitSegment * countBasicUnits);
        var endArcRadius   = TWO_PI * (0.5 + radiusUnitAuthor / 2 + interstice * (countSegments + 1) + radiusUnitSegment * (countBasicUnits + currCountBasicUnits));
        //        console.log("startArcRadius: "+startArcRadius);
        //        console.log("endArcRadius: "+endArcRadius);

        xLine1 = (SKETCH_CENTER_X
            + Math.cos(endArcRadius)
            * (INNER_CIRCLE_RADIUS));
    
        yLine1 = SKETCH_CENTER_Y
        + Math.sin(endArcRadius)
        * (INNER_CIRCLE_RADIUS + INTERSTICE_LABEL);
   
        segmentsArr[i] = new Kinetic.Shape({
            drawFunc: function(context) {
                context.beginPath();
                context.arc(SKETCH_CENTER_X, SKETCH_CENTER_Y, OUTER_CIRCLE_RADIUS, startArcRadius, endArcRadius, false);
                context.arc(SKETCH_CENTER_X, SKETCH_CENTER_Y, INNER_CIRCLE_RADIUS,endArcRadius, startArcRadius, true);
                context.closePath();
                this.fill(context);
            //                            this.stroke(context);
            },
            fill: listColors[i],
            stroke: 'black',
            strokeWidth: 4,
            opacity:0.9
            

        });
        
        segmentsArr[i].on("mouseover", function() {
            for (var j = 0;j<segmentsArr.length;j++){
                segmentsArr[j].setOpacity(opacityDecrease);
                bezierArr[j].setOpacity(opacityDecrease);
                labelsArr[j].setOpacity(opacityDecrease);
            }
            segmentsArr[i].setOpacity(1);
            bezierArr[i].setOpacity(1);
            labelsArr[i].setOpacity(1);
            layerArcBandsRegular.draw();
            layerLabelSegments.draw();

        });

        segmentsArr[i].on("mouseout", function() {
            for (var j = 0;j<segmentsArr.length;j++){
                segmentsArr[j].setOpacity(0.9);
                bezierArr[j].setOpacity(0.9);
                labelsArr[j].setOpacity(0.9);
            }
            layerArcBandsRegular.draw();
            layerLabelSegments.draw();

        });

        segmentsArr[i].on("click", function() {
            currClickedAuthor = labelsArr[i].getText();
            console.log("author clicked: "+currClickedAuthor);
            console.log("count of docs for this co-author: "+segments[i].count);

            sendNameClicked([{
                name: 'nameClicked', 
                value: currClickedAuthor
            },{
                name:'countDocs',
                value:segments[i].count
            }]);
            updateDialog();
        
            setTimeout(displayDialogJS,150);

            $("#dialogTest").dialog({
                resizable: false,
                height:140,
                modal: true,
                buttons: {
                    "Delete all items": function() {
                        $( this ).dialog( "close" );
                    },
                    "Cancel": function() {
                        $( this ).dialog( "close" );
                    }
                }
            });
            
        });


        layerArcBandsRegular.add(segmentsArr[i]);
        
    }


    function funcDrawBezier(i){
        var strokeThickness = segments[i].count * MAXSTROKEWEIGHT / segments.length;

        var x2 = (SKETCH_CENTER_X
            + Math.cos(theta)
            * (INNER_CIRCLE_RADIUS - INTERSTICE_BEZIER));
        var y2 = SKETCH_CENTER_Y
        + Math.sin(theta)
        * (INNER_CIRCLE_RADIUS - INTERSTICE_BEZIER);

    
        var hx1 = x1 + (x2 - x1) * (1 / 4);
        var hy1 = y1 + ((y2 - y1) / ((x2 - x1)) * (hx1 - x1)) - ((y2 - y1) / 4);
        var hx2 = x1 + (x2 - x1) * (4 / 5);
        var hy2 = (y1 + ((y2 - y1) / (x2 - x1)) * (hx2 - x1)) - ((y2 - y1) / 3);

        bezierArr[i] = new Kinetic.Shape({
            drawFunc: function(context) {
                context.beginPath();
                context.moveTo(x1,y1);
                context.bezierCurveTo(hx1, hy1, hx2, hy2, x2, y2);
                //                context.closePath();
                //                this.fill(context);
                this.stroke(context);
            },
            fill: listColors[i],
            stroke: 'red',
            strokeWidth: strokeThickness

        });
        layerLabelSegments.add(bezierArr[i]);

    }
    
    function funcDrawLabel(i){

        //        console.log("theta: "+theta);
        var thetaLabel;
    
        //           println("theta: " + theta);
        //           println("segment: " + segments.get(i).label);
    
        var minLimit = (3 / 4 * TWO_PI);
        var maxLimit = (5 / 4 * TWO_PI);
        var offsetLabel;
        var allo = 'left';
    
        //this condition orients the labels for easier read
        if (theta < minLimit | theta > maxLimit) {
            thetaLabel = theta + PI;
            offsetLabel = widthLabel;
            allo = 'right';
        //                textAlign(RIGHT,CENTER);
        } else {
            thetaLabel = theta;
            offsetLabel = 0;
            allo = 'left';

        //                textAlign(LEFT,CENTER);
        }

        //COORDINATES OF THE LABEL FOR THE CURRENT SEGMENT
        var xLabelSegment = (SKETCH_CENTER_X
            + Math.cos(theta)
            * (OUTER_CIRCLE_RADIUS + INTERSTICE_LABEL + offsetLabel));
        var yLabelSegment = SKETCH_CENTER_Y
        + Math.sin(theta)
        * (OUTER_CIRCLE_RADIUS + INTERSTICE_LABEL + offsetLabel);
     
        labelsArr[i] = new Kinetic.Text({
            x: xLabelSegment,
            y: yLabelSegment,
            text: segments[i].label,
            align:allo,
            width:widthLabel,
            offset:[0,(textSize/2)],
            fontSize: textSize,
            fontFamily: 'Calibri',
            textFill: 'black',
            rotation: thetaLabel
         
        });

        labelsArr[i].on("mouseover", function() {
            for (var j = 0;j<segmentsArr.length;j++){
                labelsArr[j].setOpacity(opacityDecrease);
                bezierArr[j].setOpacity(opacityDecrease);
                segmentsArr[j].setOpacity(opacityDecrease);
            }
            segmentsArr[i].setOpacity(1);
            bezierArr[i].setOpacity(1);
            labelsArr[i].setOpacity(1);
            layerArcBandsRegular.draw();
            layerLabelSegments.draw();

        });

        labelsArr[i].on("mouseout", function() {
            for (var j = 0;j<segmentsArr.length;j++){
                segmentsArr[j].setOpacity(0.9);
                bezierArr[j].setOpacity(0.9);
                labelsArr[j].setOpacity(0.9);
            }
            layerArcBandsRegular.draw();
            layerLabelSegments.draw();

        });

        labelsArr[i].on("click", function() {
            currClickedAuthor = labelsArr[i].getText();
            console.log("author clicked: "+currClickedAuthor);
            console.log("count of docs for this co-author: "+segments[i].count);

            
            sendNameClicked([{
                name: 'nameClicked', 
                value: currClickedAuthor
            },{
                name:'countDocs',
                value:segments[i].count
            }]);
            updateDialog();
        
        });



        layerLabelSegments.add(labelsArr[i]);
    }
    

    function getCurrClickedAuthor(){
        return currClickedAuthor;
    }

    function createColors(){
        listColors.push("#E78B27");
        listColors.push("#6130D5");
        listColors.push("#7FE5E5");
        listColors.push("#59E341");
        listColors.push("#2B2342");
        listColors.push("#E03C86");
        listColors.push("#3D5D21");
        listColors.push("#A48BD9");
        listColors.push("#E7B39C");
        listColors.push("#71211B");
        listColors.push("#DDE238");
        listColors.push("#D1E693");
        listColors.push("#E565DE");
        listColors.push("#E7432B");
        listColors.push("#3F837B");
        listColors.push("#50B376");
        listColors.push("#A5953F");
        listColors.push("#4596BF");
        listColors.push("#A1606A");
        listColors.push("#422592");
        listColors.push("#E898C3");
        listColors.push("#90613D");
        listColors.push("#C6D4DE");
        listColors.push("#501C65");
        listColors.push("#9B87A3");
        listColors.push("#233119");
        listColors.push("#5A1B37");
        listColors.push("#9E3397");
        listColors.push("#E1BB39");
        listColors.push("#E47772");
        listColors.push("#BE31ED");
        listColors.push("#78E37D");
        listColors.push("#2F507D");
        listColors.push("#405EB4");
        listColors.push("#86A52E");
        listColors.push("#BDEABF");
        listColors.push("#6159D8");
        listColors.push("#5C99E6");
        listColors.push("#1D3940");
        listColors.push("#E0375F");
        listColors.push("#E57647");
        listColors.push("#4AB238");
        listColors.push("#65E9B6");
        listColors.push("#A0E13A");
        listColors.push("#EBC787");
        listColors.push("#89AA5E");
        listColors.push("#D6BAE3");
        listColors.push("#DE65B5");
        listColors.push("#C28540");
        listColors.push("#8D8457");
        listColors.push("#A4998C");
        listColors.push("#9A46CC");
        listColors.push("#962D70");
        listColors.push("#7C50A9");
        listColors.push("#AF3539");
        listColors.push("#DF31AD");
        listColors.push("#C5DE65");
        listColors.push("#58BAA8");
        listColors.push("#693D65");
        listColors.push("#6B5558");
        listColors.push("#A63B5C");
        listColors.push("#471F16");
        listColors.push("#3F802C");
        listColors.push("#4F4518");
        listColors.push("#E2DDC0");
        listColors.push("#336B48");
        listColors.push("#AE4418");
        listColors.push("#A560A2");
        listColors.push("#6A7EEB");
        listColors.push("#2A2E6A");
        listColors.push("#DA6E97");
        listColors.push("#D79096");
        listColors.push("#595842");
        listColors.push("#DFB8C3");
        listColors.push("#AE7F6E");
        listColors.push("#8FBA8C");
        listColors.push("#97B9B1");
        listColors.push("#6F85BB");
        listColors.push("#2D6A85");
        listColors.push("#D336D7");
        listColors.push("#6FC6E9");
        listColors.push("#DF9CE3");
        listColors.push("#33282A");
        listColors.push("#C779E1");
        listColors.push("#655E9A");
        listColors.push("#7E4840");
        listColors.push("#61A5B3");
        listColors.push("#794911");
        listColors.push("#BDB486");
        listColors.push("#396062");
        listColors.push("#9F6A90");
        listColors.push("#595973");
        listColors.push("#756F22");
        listColors.push("#A75837");
        listColors.push("#D79B69");
        listColors.push("#7B878B");
        listColors.push("#B5841D");
        listColors.push("#A3B0DC");
        listColors.push("#648B69");
        listColors.push("#DCC168");
        listColors.push("#E78B27");
        listColors.push("#6130D5");
        listColors.push("#7FE5E5");
        listColors.push("#59E341");
        listColors.push("#2B2342");
        listColors.push("#E03C86");
        listColors.push("#3D5D21");
        listColors.push("#A48BD9");
        listColors.push("#E7B39C");
        listColors.push("#71211B");
        listColors.push("#DDE238");
        listColors.push("#D1E693");
        listColors.push("#E565DE");
        listColors.push("#E7432B");
        listColors.push("#3F837B");
        listColors.push("#50B376");
        listColors.push("#A5953F");
        listColors.push("#4596BF");
        listColors.push("#A1606A");
        listColors.push("#422592");
        listColors.push("#E898C3");
        listColors.push("#90613D");
        listColors.push("#C6D4DE");
        listColors.push("#501C65");
        listColors.push("#9B87A3");
        listColors.push("#233119");
        listColors.push("#5A1B37");
        listColors.push("#9E3397");
        listColors.push("#E1BB39");
        listColors.push("#E47772");
        listColors.push("#BE31ED");
        listColors.push("#78E37D");
        listColors.push("#2F507D");
        listColors.push("#405EB4");
        listColors.push("#86A52E");
        listColors.push("#BDEABF");
        listColors.push("#6159D8");
        listColors.push("#5C99E6");
        listColors.push("#1D3940");
        listColors.push("#E0375F");
        listColors.push("#E57647");
        listColors.push("#4AB238");
        listColors.push("#65E9B6");
        listColors.push("#A0E13A");
        listColors.push("#EBC787");
        listColors.push("#89AA5E");
        listColors.push("#D6BAE3");
        listColors.push("#DE65B5");
        listColors.push("#C28540");
        listColors.push("#8D8457");
        listColors.push("#A4998C");
        listColors.push("#9A46CC");
        listColors.push("#962D70");
        listColors.push("#7C50A9");
        listColors.push("#AF3539");
        listColors.push("#DF31AD");
        listColors.push("#C5DE65");
        listColors.push("#58BAA8");
        listColors.push("#693D65");
        listColors.push("#6B5558");
        listColors.push("#A63B5C");
        listColors.push("#471F16");
        listColors.push("#3F802C");
        listColors.push("#4F4518");
        listColors.push("#E2DDC0");
        listColors.push("#336B48");
        listColors.push("#AE4418");
        listColors.push("#A560A2");
        listColors.push("#6A7EEB");
        listColors.push("#2A2E6A");
        listColors.push("#DA6E97");
        listColors.push("#D79096");
        listColors.push("#595842");
        listColors.push("#DFB8C3");
        listColors.push("#AE7F6E");
        listColors.push("#8FBA8C");
        listColors.push("#97B9B1");
        listColors.push("#6F85BB");
        listColors.push("#2D6A85");
        listColors.push("#D336D7");
        listColors.push("#6FC6E9");
        listColors.push("#DF9CE3");
        listColors.push("#33282A");
        listColors.push("#C779E1");
        listColors.push("#655E9A");
        listColors.push("#7E4840");
        listColors.push("#61A5B3");
        listColors.push("#794911");
        listColors.push("#BDB486");
        listColors.push("#396062");
        listColors.push("#9F6A90");
        listColors.push("#595973");
        listColors.push("#756F22");
        listColors.push("#A75837");
        listColors.push("#D79B69");
        listColors.push("#7B878B");
        listColors.push("#B5841D");
        listColors.push("#A3B0DC");
        listColors.push("#648B69");
        listColors.push("#DCC168");

    }


};