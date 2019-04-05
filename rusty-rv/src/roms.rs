
use crate::defs::Address;
use crate::memory::{MemoryDevice, MemoryAccessResult};
use bytes::Bytes;


pub struct ROMDevice {
	data: Bytes,
}

impl MemoryDevice for ROMDevice {
	fn device_size(&self) -> usize {
		return self.data.len()
	}

	fn read_at(&self, addr: Address, amount: Address) -> MemoryAccessResult {
		if addr > self.device_size() as u64 || (addr+amount) > self.device_size() as u64 {
			return MemoryAccessResult::InvalidAddress
		}

		return MemoryAccessResult::SuccessfulRead(self.data.slice(addr as usize, (addr+amount) as usize))
	}

	fn write_at(&mut self, addr: Address, amount: Address) -> MemoryAccessResult {
		return MemoryAccessResult::WriteFailAt(vec![addr..=(addr+amount)]) // is immutable, always fails.
	}
}
