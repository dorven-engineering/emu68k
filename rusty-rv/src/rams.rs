use crate::defs::Address;
use crate::memory::{MemoryAccessResult, MemoryDevice};
use bytes::{Bytes, BytesMut};

pub struct RAMDevice {
	data: Bytes,
}

impl RAMDevice {
	fn test_access_addr(&self, addr: Address, amount: Address) -> bool {
		addr > self.device_size() as u64
			|| (addr + amount) > self.device_size() as u64
	}
}

impl MemoryDevice for RAMDevice {
	fn device_size(&self) -> usize {
		self.data.len()
	}

	fn read_at(
		&self,
		addr: Address,
		amount: Address,
	) -> MemoryAccessResult {
		if self.test_access_addr(addr, amount) {
			MemoryAccessResult::InvalidAddress
		} else {
			MemoryAccessResult::SuccessfulRead(self.data.slice(
				addr as usize,
				(addr + amount) as usize,
			))
		}
	}

	fn write_at(
		&mut self,
		addr: Address,
		amount: Address,
	) -> MemoryAccessResult {
		if self.test_access_addr(addr, amount) {
			MemoryAccessResult::InvalidAddress
		} else {
			MemoryAccessResult::SuccessfulWrite(
				self.data
					.slice(
						addr as usize,
						(addr + amount) as usize,
					)
					.try_mut()
					.unwrap(), // dab
			)
		}
	}
}
