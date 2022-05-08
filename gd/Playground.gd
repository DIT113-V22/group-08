extends Spatial
#	variable to count the race loops
var loops = 0
#	message topic
var topic = "/IslandRush/Mood/Race/Finish"
#	message text
var message = "Race Ended"

func init_cam_pos() -> Transform:
	# connecting to mqtt here because where else?
	$mqtt.connect_to_server(true)
	return $CamPosition.global_transform

func get_spawn_position(hint: String) -> Transform:
	match hint:
		"debug_vehicle": return $DebugVehicleSpawn.global_transform
		_: return $VehicleSpawn.global_transform
	
func _on_FinishLine_CrossedFinishLine():
	$mqtt.publish(topic, message, false, 1)
