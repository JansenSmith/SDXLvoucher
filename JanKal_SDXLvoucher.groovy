import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import eu.mihosoft.vrl.v3d.*
import eu.mihosoft.vrl.v3d.svg.*
import eu.mihosoft.vrl.v3d.CSG
import javafx.scene.text.Font;

File f = ScriptingEngine
	.fileFromGit(
		"https://github.com/JansenSmith/SDXLvoucher.git",//git repo URL
		"refs/heads/main",//branch
		"JanKal_SDXLvoucher.svg"// File from within the Git repo
	)
println "Extruding SVG "+f.getAbsolutePath()
SVGLoad s = new SVGLoad(f.toURI())
println "Layers= "+s.getLayers()
// A map of layers to polygons
HashMap<String,List<Polygon>> polygonsByLayer = s.toPolygons()
// extrude all layers to a map to 10mm thick
//HashMap<String,ArrayList<CSG>> csgByLayers = s.extrudeLayers(10)
// The string represents the layer name in Inkscape
def border = s.extrudeLayerToCSG(7,"Border")
CSG voucher = border
def ticket = s.extrudeLayerToCSG(10,"Ticket")
voucher = voucher.union(ticket)
def lines = s.extrudeLayerToCSG(3,"Lines")
lines = lines.movez(7)
voucher = voucher.difference(lines)
def text = s.extrudeLayerToCSG(3,"Text")
text = text.movez(7).movey(1)
voucher = voucher.difference(text)
def emblem = s.extrudeLayerToCSG(3,"Emblem")
emblem = emblem.movez(7).movey(1)
voucher = voucher.difference(emblem)

//Font ribbonFont = new Font("Arial",  5)
Font ribbonFont = new Font("Constantia Bold",  4)
Font enjoyFont = new Font("Caladea Italic",  6.5)
Font nameFont = new Font("Caladea Italic",  7.5)

//CSG ribbonText = CSG.unionAll(TextExtrude.text((double)3.0,"ONE MEAL",ribbonFont))
//	.rotx(180).toZMin().moveToCenterX().moveToCenterY()
//	.movex(text.getCenterX())
//	.movey(text.getCenterY()-3.5)
//	.movez(4)
//voucher = voucher.difference(ribbonText)

//CSG enjoyText = CSG.unionAll(TextExtrude.text((double)3.0,"ENJOY A MEAL",enjoyFont))
//	.rotx(180).toZMin().moveToCenterX().toYMax()
//	.movex(text.getCenterX())
//	.movey(lines.getMaxY())
//	.movez(7)
//voucher = voucher.difference(enjoyText)

//CSG usText = CSG.unionAll(TextExtrude.text((double)3.0,"ON US",enjoyFont))
//	.rotx(180).toZMin().moveToCenterX().toYMin()
//	.movex(text.getCenterX())
//	.movey(lines.getMinY())
//	.movez(7)
//voucher = voucher.difference(usText)

CSG danyelText = CSG.unionAll(TextExtrude.text((double)3.0,"Danyel",nameFont))
	.rotx(180).rotz(90).toZMin().toXMax().moveToCenterY()
	.movex(lines.getMinX()-3.5)
	.movey(ticket.getCenterY())
	.movez(7)
voucher = voucher.difference(danyelText)

CSG jansenText = CSG.unionAll(TextExtrude.text((double)3.0,"Jansen",nameFont))
	.rotx(180).rotz(-90).toZMin().toXMin().moveToCenterY()
	.movex(lines.getMaxX()+3.5)
	.movey(ticket.getCenterY())
	.movez(7)
voucher = voucher.difference(jansenText)

voucher = voucher.moveToCenter().toZMin().setColor(javafx.scene.paint.Color.MAGENTA)

voucher1 = voucher.setName("voucher1").clone()
voucher2 = voucher.setName("voucher2").clone()
voucher3 = voucher.setName("voucher3").clone()
voucher4 = voucher.setName("voucher4").clone()

//println(playfulFont.getFontNames())

return [voucher1
, voucher2, voucher3, voucher4]
//return ribbonText
