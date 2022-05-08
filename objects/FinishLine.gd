extends RigidBody

signal CrossedFinishLine

func _on_FinishLine_body_entered(body):
	emit_signal("CrossedFinishLine")
