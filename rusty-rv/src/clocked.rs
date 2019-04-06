pub trait ClockedDevice {
	fn tick(&self);
}
pub struct CycleManager {
	devices: Vec<Box<dyn ClockedDevice>>,
}
impl CycleManager {
	fn register_device(&mut self, device: Box<ClockedDevice>) {
		self.devices.push(device);
	}
	fn tick_devices(&self) {
		for d in self.devices.iter() {
			d.tick();
		}
	}
	fn do_ticks(&self, count: u32) {
		for _ in 1..count {
			self.tick_devices();
		}
	}
}
