extends Spatial

func _ready():
	$AnimationPlayer.get_animation("Take 001").set_loop(true)
	$AnimationPlayer.play("Take 001")

