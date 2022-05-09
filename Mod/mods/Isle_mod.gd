extends Reference

var mod_name: String = "Isle"

func init(global) -> void:

	global.register_environment("Isle/Isle", preload("res://src/environments/Isle/Isle.tscn"))
	
	global.register_vehicle("Catcar", preload("res://src/vehicles/stupid_car/Catcar.tscn"))





