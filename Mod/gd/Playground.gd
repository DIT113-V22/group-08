extends Spatial

func init_cam_pos() -> Transform:
	return $CamPosition.global_transform
	
#cycle through different vehicle labels
func get_spawn_position(hint: String) -> Transform:
	if hint.begins_with("tropical_car"): return $VehicleSpawn.global_transform
	if hint.begins_with("finish_line"): return $FinishLine.global_transform
	else: return $DebugVehicleSpawn.global_transform
