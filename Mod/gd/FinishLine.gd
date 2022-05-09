extends Area

signal CrossedFinishLine

func _on_FinishLine_body_entered(_body):
	emit_signal("CrossedFinishLine")
