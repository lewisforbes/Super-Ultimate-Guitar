<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="stylesheet.css">

<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){

    $("#submit").click(function(e) {
        var transposevalue = $("#transpose").val();
        var sizevalue =  $("#size").val();
        window.location.replace("http://localhost:8080/SuperUltimateGuitar/song?transpose="+transposevalue+"&size="+sizevalue);
    });
});
</script>

</head>
<body>
<center>
    <h1>
        <a href="http://lewis-forbes.us-east-2.elasticbeanstalk.com/guitar/" style="color:inherit">Super Ultimate Guitar</a>
    </h1>
    <%
    String transposedBy = "" + (Integer) request.getAttribute("transposedBy");
    String givenSize = "" + (Integer) request.getAttribute("size");
    %>
    <div>
        <label for="transpose">Transpose by: </label>
        <input type="number" value="<%= transposedBy %>" id="transpose" style="width:35px;" min="-999" max="999">
        <br><br>
        <label for="size">Font size: </label>
        <input type="number" value="<%= givenSize %>" id="size" style="width:35px;" min="0" max="999">
        <br><br>
        <button type="button" id="submit">Update!</button>
    </div>
</center>
<div>
<p class="song" style="font-family:monospace; color:black; font-size:<%= givenSize %>px;">
<%
    String song = (String) request.getAttribute("song");
    String[] songLines = song.split("\n");
    for (String line : songLines) {
        out.println(line.replace(" ", "&#160;") + "<br>");
    }
%>
</p>
</div>
<br><br>
<center>
    <h2 style="font-size:x-large">Hefty merci (and d&eacute;sol&eacute;) to</h2>
    <a href="https://www.ultimate-guitar.com/">Ultimate Guitar</a><br><br>
    <div>
        <br><br>
        <p>Made by <a href="https://lewisforbes.com/">Lewis Forbes</a></p>
        <p>Code available on <a href="https://github.com/lewisforbes/Super-Ultimate-Guitar">GitHub</a></p>
    </div>
</center>
</body>
</html>