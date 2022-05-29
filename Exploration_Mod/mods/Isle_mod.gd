extends Reference

var mod_name: String = "Isle"

func init(global) -> void:

	global.register_environment("Isle/Isle", preload("res://src/environments/Isle/Isle.tscn"))
	global.register_vehicle("Catcar", preload("res://src/vehicles/stupid_car/Catcar.tscn"))
	global.register_vehicle("Elevator", preload("res://src/vehicles/elevator_car/ElevatorCar.tscn"))
	global.register_vehicle("FinishLine", preload("res://src/vehicles/finish_line/FinishLine.tscn"))
