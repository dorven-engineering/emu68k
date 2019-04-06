extern crate bytes;

pub mod clocked;
pub mod defs;
pub mod memory;
pub mod rams;
pub mod roms;

#[cfg(test)]
mod tests {
	#[test]
	fn it_works() {
		assert_eq!(2 + 2, 4);
	}
}
