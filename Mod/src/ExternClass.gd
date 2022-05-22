extends Node

export var extern_class: String = ""

func _ready():
	var global = get_tree().root.get_node_or_null("Global")
	if global == null:
		return

	var script = global.classes.get(extern_class, null)
	if script == null:
		printerr("Extern class '%s' does not exist" % extern_class)
		return
	
	# Save already set properties on the mod side
	var orig_props: Dictionary = {}
	for prop in get_property_list():
		if prop["name"] != "script":
			orig_props[prop["name"]] = get(prop["name"])
	
	# Set script to foreign script (resets all props)
	set_script(script)

	# Restore properies
	for prop in orig_props:
		set(prop, orig_props[prop])
	
	# Call ready of foreign script
	_ready()
