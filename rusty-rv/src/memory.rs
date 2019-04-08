use crate::defs::{AddrRange, Address};
use bytes::{Bytes, BytesMut};
use either::Either;

#[deprecated(note = "Not used in new code. :3")]
pub enum MemoryAccessResult {
	SuccessfulRead(Bytes),
	SuccessfulWrite(BytesMut),
	InvalidAddress,
	DeviceFailure,
	WriteFailAt(Vec<AddrRange>), /* Writes and reads can fail at
	                              * multiple positions for multiple
	                              * ranges of addresses. */
	ReadFailAt(Vec<AddrRange>),
}
#[deprecated(note = "Use ManagedMemoryDevice and WritableManagedMemoryDevice")]
pub trait MemoryDevice {
	fn device_size(&self) -> usize;

	fn read_at(
		&self,
		addr: Address,
		amount: Address,
	) -> MemoryAccessResult;

	fn write_at(
		&mut self,
		addr: Address,
		amount: Address,
	) -> MemoryAccessResult;
}

pub enum MemoryReadFailureReason {
	OutOfBounds,
	DeviceFailure,
	UnsupportedOperaton,
	InternalError(/*TODO*/),
}

pub trait ManagedMemoryDevice {
	fn get_readable(
		&mut self,
		addr: Address,
		len: Address,
	) -> Result<Bytes, MemoryReadFailureReason>;

	fn try_get_writeable(
		&mut self,
		addr: Address,
		len: Address,
	) -> Result<Either<BytesMut, Bytes>, MemoryReadFailureReason> {
		unimplemented!("TODO: make this just call get_readable and try and return a Bytes")
	}
	fn get_writeable(
		&mut self,
		addr: Address,
		len: Address,
	) -> Result<BytesMut, MemoryReadFailureReason> {
		unimplemented!("TODO: make this just call get_readable and try and return a Bytes")
	}
}

pub enum AccessMode {
	User,
	Supervisor,
	Machine,
}

pub enum TranslationResult {
	Success(Address),
	Failure,
}

pub trait VirtualMemoryHandler<VirtStructure> {
	fn translate_address(
		&self,
		translation_tree: VirtStructure,
		mode: AccessMode,
		addr: Address,
	) -> TranslationResult;
}

pub enum MemoryAccessFailure {
	VirtualLookupFailed,
	LocationReadOnly,
	LocationWriteOnly,
	LocationEmpty,
	InternalError(/* TODO */),
}

pub trait MemoryManager<VirtStructure> {
	fn get_mut_virtual_memory_handler(
		&mut self,
	) -> &mut VirtualMemoryHandler<VirtStructure>;
	fn get_virtual_memory_handler(
		&self,
	) -> &VirtualMemoryHandler<VirtStructure>;

	fn toggle_virtual_memory(&mut self) {
		self.set_virtual_memory_enabled(
			!self.get_virtual_memory_enabled(),
		);
	}
	fn get_virtual_memory_enabled(&self) -> bool;
	fn set_virtual_memory_enabled(&self, state: bool);

	fn read_u8(&self, addr: Address) -> Result<u8, MemoryAccessFailure>;
	fn read_u16(&self, addr: Address) -> Result<u16, MemoryAccessFailure>;
	fn read_u32(&self, addr: Address) -> Result<u32, MemoryAccessFailure>;
	fn read_u64(&self, addr: Address) -> Result<u64, MemoryAccessFailure>;
	fn read_u128(
		&self,
		addr: Address,
	) -> Result<u128, MemoryAccessFailure>;

	fn write_u8(
		&mut self,
		addr: Address,
		value: u8,
	) -> Option<MemoryAccessFailure>;
	fn write_u16(
		&mut self,
		addr: Address,
		value: u16,
	) -> Option<MemoryAccessFailure>;
	fn write_u32(
		&mut self,
		addr: Address,
		value: u32,
	) -> Option<MemoryAccessFailure>;
	fn write_u64(
		&mut self,
		addr: Address,
		value: u64,
	) -> Option<MemoryAccessFailure>;
	fn write_u128(
		&mut self,
		addr: Address,
		value: u128,
	) -> Option<MemoryAccessFailure>;
}
